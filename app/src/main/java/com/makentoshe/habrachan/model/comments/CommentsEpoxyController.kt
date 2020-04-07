package com.makentoshe.habrachan.model.comments

import android.util.SparseArray
import androidx.core.util.forEach
import androidx.core.util.valueIterator
import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.comment.Comment

class CommentsEpoxyController(private val factory: CommentEpoxyModel.Factory) : EpoxyController() {

    private var comments = SparseArray<ArrayList<Comment>>()

    fun setComments(comments: SparseArray<ArrayList<Comment>>) {
        this.comments = comments
    }

    fun updateCommentScore(commentId: Int, score: Int) {
        comments.forEach { key, list ->
            if (updateCommentsBranch(key, list, commentId, score)) return requestModelBuild()
        }
    }

    private fun updateCommentsBranch(key: Int, comments: ArrayList<Comment>, commentId: Int, score: Int): Boolean {
        val comment = comments.find { it.id == commentId } ?: return false
        val index = comments.indexOf(comment)
        comments.removeAt(index)
        comments.add(index, comment.copy(score = score))
        return true
    }

    override fun buildModels() {
        comments.valueIterator().forEach(::buildCommentsBranchModel)
    }

    private fun buildCommentsBranchModel(comments: List<Comment>) {
        comments.forEach(::buildCommentModel)
    }

    private fun buildCommentModel(comment: Comment) {
        factory.build(comment).addTo(this)
    }
}