package com.makentoshe.habrachan.application.android.screen.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.davemorrissey.labs.subscaleview.ImageSource
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.screen.content.model.ContentActionBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.content.model.ContentFilesystem
import com.makentoshe.habrachan.application.android.screen.content.viewmodel.ContentViewModel
import com.makentoshe.habrachan.network.response.ImageResponse
import kotlinx.android.synthetic.main.fragment_content_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifIOException
import toothpick.ktp.delegate.inject

class ContentFragmentPage : CoreFragment() {

    companion object {

        private const val EXTERNAL_STORAGE_REQUEST_CODE = 1

        fun build(source: String) = ContentFragmentPage().apply {
            arguments.source = source
        }
    }

    override val arguments = Arguments(this)

    private lateinit var exceptionController: ExceptionController

    private val viewModel by inject<ContentViewModel>()
    private val exceptionHandler by inject<ExceptionHandler>()
    private val contentFilesystem by inject<ContentFilesystem>()
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

        if (savedInstanceState == null) lifecycleScope.launch {
            viewModel.sourceChannel.send(ContentViewModel.ImageSpec(arguments.source))
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
                viewModel.sourceChannel.send(ContentViewModel.ImageSpec(arguments.source))
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
        val response = viewModel.response.valueOrNull as? ImageResponse
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

    private fun onActionDownload(imageResponse: ImageResponse) {
        contentFilesystem.saveContent(imageResponse)
    }

    private fun onActionShare(response: ImageResponse) {
        println("Share")
    }

    private fun onContentLoadingSuccess(response: ImageResponse) {
        exceptionController.hide()
        fragment_content_progress.visibility = View.GONE

        try {
            onGifLoadingSuccess(response)
        } catch (gioe: GifIOException) { // not a gif
            onImageLoadingSuccess(response)
        }
    }

    private fun onGifLoadingSuccess(response: ImageResponse) {
        fragment_content_gif.setImageDrawable(GifDrawable(response.bytes).apply { start() })
        fragment_content_image.visibility = View.GONE
        fragment_content_gif.visibility = View.VISIBLE
    }

    private fun onImageLoadingSuccess(response: ImageResponse) {
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

    class Arguments(fragment: ContentFragmentPage) : CoreFragment.Arguments(fragment) {

        var source: String
            set(value) = fragmentArguments.putString(SOURCE, value)
            get() = fragmentArguments.getString(SOURCE)!!

        companion object {
            private const val SOURCE = "ImageSource"
        }
    }
}
