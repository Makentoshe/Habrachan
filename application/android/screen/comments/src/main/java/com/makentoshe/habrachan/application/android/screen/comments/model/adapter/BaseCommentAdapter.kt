package com.makentoshe.habrachan.application.android.screen.comments.model.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentDiffUtilItemCallback

abstract class BaseCommentAdapter<ViewHolder : RecyclerView.ViewHolder> :
    PagingDataAdapter<CommentModelElement, ViewHolder>(CommentDiffUtilItemCallback())