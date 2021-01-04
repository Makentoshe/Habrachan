package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment

/** Is a node of the comments tree */
data class CommentModel(val comment: Comment) {
    var parent: CommentModel? = null
    val childs =  ArrayList<CommentModel>()
}

/** Returns a tree with multiple roots */
fun buildModelsFromList(comments: List<Comment>): List<CommentModel> {
    val models = comments.map { CommentModel(it) }
    val map = comments.map { it.id }.zip(models).toMap()
    models.forEach { model ->
        if (model.comment.parentId != 0) {
            model.parent = map[model.comment.parentId]
            model.parent?.childs?.add(model)
        }
    }
    return models
}