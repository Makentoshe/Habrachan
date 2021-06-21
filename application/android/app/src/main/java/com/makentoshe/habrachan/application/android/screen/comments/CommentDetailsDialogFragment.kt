package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.core.fragment.BaseBottomSheetDialogFragment
import com.makentoshe.habrachan.application.android.common.core.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class CommentDetailsDialogFragment : BaseBottomSheetDialogFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(commentId: Int) = CommentDetailsDialogFragment().apply {
            arguments.commentId = commentId
        }
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<CommentDetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_comment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) lifecycleScope.launch(Dispatchers.IO) {
            capture(analyticEvent(javaClass.simpleName, "Dialog shown"))
            viewModel.commentRequestChannel.send(CommentDetailsViewModel.CommentRequest(arguments.commentId))
        }

        lifecycleScope.launch {
            viewModel.commentFlow.collectLatest {
                println(it?.author?.login)
            }
        }
        lifecycleScope.launch {
            viewModel.avatarFlow.collectLatest {
                println(it)
            }
        }
    }

    class Arguments(fragment: CommentDetailsDialogFragment) : FragmentArguments(fragment) {

        var commentId: Int
            get() = fragmentArguments.getInt(COMMENT_ID)
            set(value) = fragmentArguments.putInt(COMMENT_ID, value)

        companion object {
            private const val COMMENT_ID = "CommentId"
        }
    }
}