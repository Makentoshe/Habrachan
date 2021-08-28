package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.content.Context
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarSpec
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.comments.R
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.onFailure
import com.makentoshe.habrachan.functional.onSuccess
import com.makentoshe.habrachan.network.GetContentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AvatarCommentAdapterControllerDecorator(
    private val commentAdapterController: CommentAdapterController?,
    private val lifecycleScope: CoroutineScope,
    private val viewModel: GetAvatarViewModel,
) : CommentAdapterController {
    override fun onBindViewHolderComment(holder: CommentViewHolder,  model: CommentModelElement) {
        commentAdapterController?.onBindViewHolderComment(holder, model)

        with(CommentViewController(holder)) {
            setCommentAvatar(holder.context, model.comment.avatar)
        }
    }

    private fun CommentViewController.setCommentAvatar(context: Context, avatar: String?) {
        if (avatar == null) body.avatar.setStubAvatar() else lifecycleScope.launch(Dispatchers.IO) {
            collectAvatar(context, avatar, this)
        }
    }

    private suspend fun CommentViewController.collectAvatar(
        context: Context,
        avatar: String,
        coroutineScope: CoroutineScope,
    ) = viewModel.requestAvatar(GetAvatarSpec(avatar)).collectLatest { result ->
        onAvatarResult(context, result, coroutineScope)
    }

    private fun CommentViewController.onAvatarResult(
        context: Context,
        response: Result<GetContentResponse>,
        coroutineScope: CoroutineScope,
    ) = response.onFailure { body.avatar.setStubAvatar() }.onSuccess {
        coroutineScope.launch(Dispatchers.Main) {
            body.avatar.setAvatar(it.bytes.toRoundedDrawable(context.resources, context.dp2px(R.dimen.radiusS)))
        }
    }

}