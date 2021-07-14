package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseCommentAdapter<ViewHolder: RecyclerView.ViewHolder>(
    private val lifecycleScope: CoroutineScope,
    private val viewModel: CommentsViewModel
): PagingDataAdapter<CommentModelElement, ViewHolder>(CommentDiffUtilItemCallback()) {

    protected fun CommentViewController.setCommentAvatar(holder: CommentViewHolder, model: CommentModelElement) {
        val avatar = model.comment.avatar
        if (avatar == null) setStubAvatar() else lifecycleScope.launch(Dispatchers.IO) {
            viewModel.requestAvatar(avatar).collectLatest { result ->
                result.onFailure { setStubAvatar() }.onSuccess {
                    val resources = holder.context.resources
                    val radius = holder.context.dp2px(R.dimen.radiusS)
                    launch(Dispatchers.Main) { setAvatar(it.bytes.toRoundedDrawable(resources, radius)) }
                }
            }
        }
    }
}