package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment

/** Is a node of the comments tree */
data class CommentModel(val comment: Comment) {
    var parent: CommentModel? = null
    val childs =  ArrayList<CommentModel>()
}