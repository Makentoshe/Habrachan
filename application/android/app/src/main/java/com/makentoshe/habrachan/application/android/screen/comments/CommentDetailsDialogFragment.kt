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
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.CommentBottomPanelController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.core.fragment.BaseBottomSheetDialogFragment
import com.makentoshe.habrachan.application.android.common.core.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentDetailsViewModel
import com.makentoshe.habrachan.application.android.toBitmap
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

        val viewHolder = CommentViewHolder(view.findViewById(R.id.dialog_comment_details_comment))
        val commentViewController = CommentViewController(viewHolder)
        val commentBodyController = CommentBodyController(viewHolder)
        val commentBottomPanelController = CommentBottomPanelController(viewHolder)
        commentBottomPanelController.hide()

        viewModel.commentFlow.foldLatest({ comment ->
            commentViewController.default(comment)
            commentBodyController.setContent(comment)
            commentBodyController.collapse()
            commentBottomPanelController.hide()
        },{ throwable ->
            println(throwable)
        })

        viewModel.avatarFlow.foldLatest({ response ->
            commentViewController.setAvatar(response.bytes.toBitmap())
        },{
            commentViewController.setStubAvatar()
        })

        viewHolder.avatarView.setOnClickListener {
            commentBodyController.expand()
        }
    }

    private fun <T> Flow<Result<T>>.foldLatest(success: (T) -> Unit, failure: (Throwable) -> Unit) {
        lifecycleScope.launch { collectLatest { it.fold(success, failure) } }
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