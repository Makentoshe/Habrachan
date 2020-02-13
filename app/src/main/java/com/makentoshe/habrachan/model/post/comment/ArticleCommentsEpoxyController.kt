package com.makentoshe.habrachan.model.post.comment

import android.util.SparseArray
import androidx.core.util.valueIterator
import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.comment.Comment
import io.reactivex.disposables.CompositeDisposable

class ArticleCommentsEpoxyController(
    private val factory: ArticleCommentEpoxyModel.Factory,
    private val disposables: CompositeDisposable
) : EpoxyController() {

    private var comments = SparseArray<ArrayList<Comment>>()

    fun setComments(comments: SparseArray<ArrayList<Comment>>) {
        this.comments = comments
    }

    override fun buildModels() {
        comments.valueIterator().forEach { comments ->
            comments.forEach { comment ->
                factory.build(comment, disposables).addTo(this)
            }
        }
    }
}