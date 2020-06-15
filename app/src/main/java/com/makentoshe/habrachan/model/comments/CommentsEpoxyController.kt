package com.makentoshe.habrachan.model.comments

import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.model.tree.Tree

class CommentsEpoxyController(private val factory: CommentEpoxyModel.Factory) : EpoxyController() {

    private var commentsTree = Tree<Comment>(arrayListOf(), arrayListOf())

    fun setComments(comments: Tree<Comment>) {
        this.commentsTree = comments
    }

    fun getCommentsTree(): Tree<Comment> = commentsTree

    fun updateCommentScore(commentId: Int, score: Int) {
        val comment = commentsTree.find { it.value.id == commentId }
        buildCommentModel(comment!!.value.copy(score = score))
        requestModelBuild()
    }

    override fun buildModels() {
        commentsTree.forEachDepthFirst { buildCommentModel(it.value) }
    }

    private fun buildCommentModel(comment: Comment) {
        factory.build(comment).addTo(this)
    }
}