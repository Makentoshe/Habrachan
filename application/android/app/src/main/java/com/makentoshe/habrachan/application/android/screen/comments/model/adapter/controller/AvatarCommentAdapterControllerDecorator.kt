package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.content.Context
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AvatarCommentAdapterControllerDecorator(
    private val commentAdapterController: CommentAdapterController?,
    private val lifecycleScope: CoroutineScope,
    private val viewModel: CommentsViewModel,
) : CommentAdapterController {
    override fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelElement) {
        commentAdapterController?.onBindViewHolderComment(holder, position, model)

        with(CommentViewController(holder)) {
            setCommentAvatar(holder.context, model.comment.avatar)
        }
    }

    private fun CommentViewController.setCommentAvatar(context: Context, avatar: String?) {
        if (avatar == null) setStubAvatar() else lifecycleScope.launch(Dispatchers.IO) {
            viewModel.requestAvatar(avatar).collectLatest { result ->
                result.onFailure { setStubAvatar() }.onSuccess {
                    launch(Dispatchers.Main) {
                        setAvatar(it.bytes.toRoundedDrawable(context.resources, context.dp2px(R.dimen.radiusS)))
                    }
                }
            }
        }
    }

}