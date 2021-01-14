package com.makentoshe.habrachan.application.android.screen.comments.model

import com.makentoshe.habrachan.entity.Comment

interface CommentModelElement {
    val comment: Comment
}

data class CommentModelBlank(override val comment: Comment): CommentModelElement {
    val parent: CommentModelNode? = null
}

data class CommentModelNode(override val comment: Comment) : CommentModelElement {
    var parent: CommentModelNode? = null
    val childs = ArrayList<CommentModelNode>()
}

data class CommentModelForest(val nodes: List<CommentModelNode>) {

    val roots = nodes.filter { model -> model.comment.parentId == 0 }

    fun findNodeByCommentId(id: Int): CommentModelNode? {
        return nodes.find { it.comment.id == id }
    }

    // At now we trust the source: the nodes is already sorted, but we should
    // TODO add an additional sorting
    fun collect(level: Int) : List<CommentModelElement> {
        val n = nodes.filter { it.comment.level <= level + 1 }
        return n.map { if (it.comment.level <= level) it else CommentModelBlank(it.comment) }
    }

    fun collect(commentId: Int, level: Int) {

    }
}

fun buildCommentModelForest(comments: List<Comment>): CommentModelForest {
    val models = comments.map { CommentModelNode(it) }
    val map = comments.map { it.id }.zip(models).toMap()
    models.forEach { model ->
        model.parent = map[model.comment.parentId]
        model.parent?.childs?.add(model)
    }
    return CommentModelForest(models)
}

sealed class CommentAdapterModel {

    data class Comment(val comment: com.makentoshe.habrachan.entity.Comment) : CommentAdapterModel()

    data class Block(val parent: Int, val actualLevel: Int, val count: Int) : CommentAdapterModel()

    companion object {

        private fun buildModels(
            comment: com.makentoshe.habrachan.entity.Comment, maxLevelIncluded: Int
        ): CommentAdapterModel {
            return if (comment.level <= maxLevelIncluded) {
                Comment(comment)
            } else {
                Block(comment.parentId, comment.level, 1)
            }
        }

        /**
         * Builds a list of models for the [CommentAdapter] for the [ArticleCommentFragment]
         *
         * Comments, which level is less than level from [commentId] plus [levelRange]
         * will be ignored.
         * Comments, which level in range between level from [commentId] and
         * (level from [commentId] plus levelRange) will be displayed.
         * Comments, which level is bigger than level from [commentId] plus [levelRange]
         * will be displayed as a Block
         * */
        fun composeArticleComments(
            comments: List<com.makentoshe.habrachan.entity.Comment>, levelRange: Int
        ): List<CommentAdapterModel> {
            val adapterModels = ArrayList<CommentAdapterModel>()
            val blocks = ArrayList<Block>()
            comments.map { comment -> buildModels(comment, levelRange) }.forEach { model ->
                if (model is Block) {
                    blocks.add(model)
                } else {
                    if (blocks.isNotEmpty()) {
                        if (blocks.size < 2) {
                            adapterModels.add(blocks.last())
                        } else {
                            blocks.reduce { acc, block ->
                                val b = if (acc.actualLevel < block.actualLevel) acc else block
                                Block(b.parent, b.actualLevel, acc.count + block.count)
                            }.let(adapterModels::add)
                        }
                    }
                    blocks.clear()
                    adapterModels.add(model)
                }; Unit
            }
            return adapterModels
        }
    }
}
