package com.makentoshe.habrachan.model.comments

import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.comments.tree.CommentsTree

class CommentsEpoxyController(private val factory: CommentEpoxyModel.Factory) : EpoxyController() {

    private var comments = CommentsTree(arrayListOf(), arrayListOf())

    fun setComments(comments: CommentsTree) {
        this.comments = comments
    }

    fun updateCommentScore(commentId: Int, score: Int) {
        val comment = comments.find { it.value.id == commentId }
        buildCommentModel(comment!!.value.copy(score = score))
        requestModelBuild()
    }

    override fun buildModels() {
        //todo update iteration from forEach to DFS
        comments.map { it.value }.forEach(::buildCommentModel)
    }

    private fun buildCommentModel(comment: Comment) {
        factory.build(comment).addTo(this)
    }
}