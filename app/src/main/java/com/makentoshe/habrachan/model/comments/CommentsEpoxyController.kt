package com.makentoshe.habrachan.model.comments

import android.util.SparseArray
import androidx.core.util.valueIterator
import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.comment.Comment

class CommentsEpoxyController(private val factory: CommentEpoxyModel.Factory) : EpoxyController() {

    private var comments = SparseArray<ArrayList<Comment>>()

    fun setComments(comments: SparseArray<ArrayList<Comment>>) {
        this.comments = comments
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