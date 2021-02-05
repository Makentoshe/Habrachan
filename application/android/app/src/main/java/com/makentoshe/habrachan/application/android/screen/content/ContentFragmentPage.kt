package com.makentoshe.habrachan.application.android.screen.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.davemorrissey.labs.subscaleview.ImageSource
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.screen.content.viewmodel.ContentViewModel
import com.makentoshe.habrachan.network.response.ImageResponse
import kotlinx.android.synthetic.main.fragment_content_page.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ContentFragmentPage : CoreFragment() {

    companion object {
        fun build(source: String) = ContentFragmentPage().apply {
            arguments.source = source
        }
    }

    override val arguments = Arguments(this)

    private lateinit var exceptionController: ExceptionController

    private val viewModel by inject<ContentViewModel>()
    private val exceptionHandler by inject<ExceptionHandler>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exceptionController = ExceptionController(ExceptionViewHolder(fragment_content_exception))

        if (savedInstanceState == null) lifecycleScope.launch {
            viewModel.sourceChannel.send(ContentViewModel.ImageSpec(arguments.source))
        }

        lifecycleScope.launch {
            viewModel.image.collectLatest { response ->
                response.fold({ onImageLoadingSuccess(it) }, { onImageLoadingFailure(it) })
            }
        }

        exceptionController.setRetryButton {
            println("Retry")
        }
    }

    private fun onImageLoadingSuccess(response: ImageResponse) {
        exceptionController.hide()
        fragment_content_image.visibility = View.VISIBLE
        fragment_content_progress.visibility = View.GONE

        fragment_content_image.setImage(ImageSource.bitmap(response.bytes.toBitmap()))
    }

    private fun onImageLoadingFailure(failure: Throwable) {
        exceptionController.render(exceptionHandler.handleException(failure))
        fragment_content_image.visibility = View.GONE
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
