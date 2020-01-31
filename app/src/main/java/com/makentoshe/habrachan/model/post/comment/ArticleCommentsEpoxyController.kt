package com.makentoshe.habrachan.model.post.comment

import android.content.Context
import android.util.SparseArray
import androidx.core.util.valueIterator
import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.comment.Comment

class ArticleCommentsEpoxyController(private val factory: ArticleCommentEpoxyModel.Factory) : EpoxyController() {

    private var comments = SparseArray<ArrayList<Comment>>()

    private var context: Context? = null

    fun setComments(comments: SparseArray<ArrayList<Comment>>) {
        this.comments = comments
    }

    fun setContext(context: Context) {
        this.context = context
    }

    override fun buildModels() {
        comments.valueIterator().forEach { comments ->
            comments.forEach { comment ->
                factory.build(comment, context!!).addTo(this)
            }
        }
    }
}