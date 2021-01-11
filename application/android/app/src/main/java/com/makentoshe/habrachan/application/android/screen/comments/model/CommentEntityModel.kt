package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment

sealed class CommentAdapterModel {

    data class Comment(val comment: com.makentoshe.habrachan.entity.Comment) : CommentAdapterModel()

    data class Block(val parent: Int, val level: Int, val count: Int) : CommentAdapterModel()
}

// todo reorganize
fun composeModels(comments: List<Comment>, maxLevelIncluded: Int): List<CommentAdapterModel> {
    val adapterModels = ArrayList<CommentAdapterModel>()
    val blocks = ArrayList<CommentAdapterModel.Block>()
    comments.map { comment ->
        if (comment.level <= maxLevelIncluded) {
            CommentAdapterModel.Comment(comment)
        } else {
            CommentAdapterModel.Block(comment.parentId, comment.level, 1)
        }
    }.forEach { model ->
        if (model is CommentAdapterModel.Block) {
            blocks.add(model)
        } else {
            if (blocks.isNotEmpty()) {
                if (blocks.size < 2) {
                    adapterModels.add(blocks.last())
                } else {
                    blocks.reduce { acc, block ->
                        val b = if (acc.level < block.level) acc else block
                        CommentAdapterModel.Block(b.parent, b.level, acc.count + block.count)
                    }.let(adapterModels::add)
                }
            }
            blocks.clear()
            adapterModels.add(model)
        }; Unit
    }
    return adapterModels
}