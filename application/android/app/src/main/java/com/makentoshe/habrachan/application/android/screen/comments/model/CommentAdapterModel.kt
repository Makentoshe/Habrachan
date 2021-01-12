package com.makentoshe.habrachan.application.android.screen.comments.model

sealed class CommentAdapterModel {

    data class Comment(val comment: com.makentoshe.habrachan.entity.Comment) : CommentAdapterModel()

    data class Block(val parent: Int, val level: Int, val count: Int) : CommentAdapterModel()

    companion object {

        private fun buildModels(comment: com.makentoshe.habrachan.entity.Comment, maxLevelIncluded: Int): CommentAdapterModel {
            return if (comment.level <= maxLevelIncluded) {
                Comment(comment)
            } else {
                Block(comment.parentId, comment.level, 1)
            }
        }

        /**
         * Builds a list of models for the [CommentAdapter]
         *
         * [comments] - source
         * [maxLevelIncluded] - if level is above this value - it will be replaced by Block structure
         * */
        fun compose(
            comments: List<com.makentoshe.habrachan.entity.Comment>,
            maxLevelIncluded: Int
        ): List<CommentAdapterModel> {
            val adapterModels = ArrayList<CommentAdapterModel>()
            val blocks = ArrayList<Block>()
            comments.map { comment -> buildModels(comment, maxLevelIncluded) }.forEach { model ->
                if (model is Block) {
                    blocks.add(model)
                } else {
                    if (blocks.isNotEmpty()) {
                        if (blocks.size < 2) {
                            adapterModels.add(blocks.last())
                        } else {
                            blocks.reduce { acc, block ->
                                val b = if (acc.level < block.level) acc else block
                                Block(b.parent, b.level, acc.count + block.count)
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
