package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement

interface CommentAdapterController {

    fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelElement)
}