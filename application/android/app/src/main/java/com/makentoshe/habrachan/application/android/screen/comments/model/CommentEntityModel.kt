package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment

/** Is a node of the comments tree */
data class CommentEntityModel(val comment: Comment) {
    var parent: CommentEntityModel? = null
    val childs =  ArrayList<CommentEntityModel>()
}