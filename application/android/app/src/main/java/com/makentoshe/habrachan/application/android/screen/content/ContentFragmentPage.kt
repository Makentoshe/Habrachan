package com.makentoshe.habrachan.application.android.screen.content

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.davemorrissey.labs.subscaleview.ImageSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.filesystem.FileSystem
import com.makentoshe.habrachan.application.android.screen.content.model.ContentActionBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.content.viewmodel.ContentViewModel
import com.makentoshe.habrachan.network.response.GetContentResponse
import kotlinx.android.synthetic.main.fragment_content_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifIOException
import toothpick.ktp.delegate.inject
import java.io.File

class ContentFragmentPage : CoreFragment() {

    companion object : Analytics(LogAnalytic()) {

        fun build(source: String) = ContentFragmentPage().apply {
            arguments.source = source
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)

    private lateinit var exceptionController: ExceptionController

    private val viewModel by inject<ContentViewModel>()
    private val exceptionHandler by inject<ExceptionHandler>()
    private val picturesFilesystem by inject<FileSystem>()
    private val contentActionBroadcastReceiver by inject<ContentActionBroadcastReceiver>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentActionBroadcastReceiver.register(requireActivity(), lifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exceptionController = ExceptionController(ExceptionViewHolder(fragment_content_exception))
        fragment_content_image.maxScale = 10f

        val wasViewModelRecreated = viewModel.toString() != savedInstanceState?.getString(VIEW_MODEL_STATE_KEY)
        if (savedInstanceState == null || wasViewModelRecreated) lifecycleScope.launch {
            capture(analyticEvent(this@ContentFragmentPage.javaClass.simpleName, "source=${arguments.source}"))
            viewModel.sourceChannel.send(ContentViewModel.ContentSpec(arguments.source))
        }

        lifecycleScope.launch {
            viewModel.content.collectLatest { response ->
                response.fold({ onContentLoadingSuccess(it) }, { onContentLoadingFailure(it) })
            }
        }

        exceptionController.setRetryButton {
            fragment_content_progress.visibility = View.VISIBLE
            exceptionController.hide()
            lifecycleScope.launch {
                viewModel.sourceChannel.send(ContentViewModel.ContentSpec(arguments.source))
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            contentActionBroadcastReceiver.channel.receiveAsFlow().collectLatest { action ->
                onActionReceive(action)
            }
        }
    }

    private fun onActionReceive(action: ContentActionBroadcastReceiver.Action) {
        // Idk how it works, but it works
        val response = viewModel.response.valueOrNull as? GetContentResponse
        if (response == null) {
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), R.string.content_error_loading, Toast.LENGTH_LONG).show()
            }
            return
        }
        when (action) {
            ContentActionBroadcastReceiver.Action.Download -> onActionDownload(response)
            ContentActionBroadcastReceiver.Action.Share -> onActionShare(response)
        }
    }

    private fun onActionDownload(imageResponse: GetContentResponse) = lifecycleScope.launch(Dispatchers.IO) {
        val permissions = listOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val listener =
            CustomBaseMultiplePermissionsListener(requireContext(), picturesFilesystem, imageResponse, lifecycleScope)
        Dexter.withContext(context).withPermissions(permissions).withListener(listener).check()
    }

    private fun onActionShare(response: GetContentResponse) {
        Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_LONG).show()
    }

    private fun onContentLoadingSuccess(response: GetContentResponse) {
        exceptionController.hide()
        fragment_content_progress.visibility = View.GONE

        try {
            onGifLoadingSuccess(response)
        } catch (gioe: GifIOException) { // not a gif
            onImageLoadingSuccess(response)
        }
    }

    private fun onGifLoadingSuccess(response: GetContentResponse) {
        fragment_content_gif.setImageDrawable(GifDrawable(response.bytes).apply { start() })
        fragment_content_image.visibility = View.GONE
        fragment_content_gif.visibility = View.VISIBLE
    }

    private fun onImageLoadingSuccess(response: GetContentResponse) {
        fragment_content_image.setImage(ImageSource.bitmap(response.bytes.toBitmap()))
        fragment_content_image.visibility = View.VISIBLE
        fragment_content_gif.visibility = View.GONE
    }

    private fun onContentLoadingFailure(failure: Throwable) {
        exceptionController.render(exceptionHandler.handleException(failure))
        fragment_content_image.visibility = View.GONE
        fragment_content_gif.visibility = View.GONE
        fragment_content_progress.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_MODEL_STATE_KEY, viewModel.toString())
    }

    private class CustomBaseMultiplePermissionsListener(
        private val context: Context,
        private val filesystem: FileSystem,
        private val response: GetContentResponse,
        private val scope: CoroutineScope
    ) : BaseMultiplePermissionsListener() {

        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if (report.areAllPermissionsGranted()) {
                onAllPermissionsGranted()
            } else {
                Toast.makeText(context, context.getString(R.string.content_error_permissions), Toast.LENGTH_LONG).show()
            }
        }

        private fun onAllPermissionsGranted() = try {
            val applicationTitle = context.getString(R.string.app_name)
            filesystem.push(File(applicationTitle, File(response.request.url).name).path, response.bytes)
            scope.launch(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.content_loading_finish), Toast.LENGTH_LONG).show()
            }
        } catch (fae: FileAlreadyExistsException) {
            scope.launch(Dispatchers.Main) {
                Toast.makeText(context, fae.message, Toast.LENGTH_LONG).show()
            }
        } catch (fse: FileSystemException) {
            scope.launch {
                Toast.makeText(context, fse.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    class Arguments(fragment: ContentFragmentPage) : CoreFragment.Arguments(fragment) {

        var source: String
            set(value) = fragmentArguments.putString(SOURCE, value)
            get() = fragmentArguments.getString(SOURCE)!!

        companion object {
            private const val SOURCE = "ImageSource"
        }
    }
}
