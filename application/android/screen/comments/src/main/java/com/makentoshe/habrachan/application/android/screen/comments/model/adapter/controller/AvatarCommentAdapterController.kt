package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.content.Context
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelFlowResult
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelRequest
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.dp2px
import com.makentoshe.habrachan.application.android.common.toRoundedDrawable
import com.makentoshe.habrachan.application.android.screen.comments.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AvatarCommentAdapterController(
    private val lifecycleScope: CoroutineScope,
    private val getAvatarViewModel: GetAvatarViewModel,
) : CommentAdapterController {

    override fun onBindViewHolderComment(holder: CommentViewHolder, model: CommentModelElement) {
        val commentViewController = CommentViewController(holder)
        commentViewController.setCommentAvatar(holder.context, model.comment.avatar)
    }

    private fun CommentViewController.setCommentAvatar(context: Context, avatar: String?) {
        if (avatar == null) body.avatar.setStubAvatar() else lifecycleScope.launch(Dispatchers.IO) {
            getAvatarViewModel.requestAvatar(GetAvatarViewModelRequest(avatar)).collectLatest { result ->
                onAvatarResult(context, result)
            }
        }
    }

    private fun CommentViewController.onAvatarResult(context: Context, response: GetAvatarViewModelFlowResult) {
        if (response.loading) return

        response.content.onRight { body.avatar.setStubAvatar() }.mapLeft { it.content.bytes }.onLeft { bytes ->
            lifecycleScope.launch(Dispatchers.Main) {
                body.avatar.setAvatar(bytes.toRoundedDrawable(context.resources, context.dp2px(R.dimen.radiusS)))
            }
        }
    }
}