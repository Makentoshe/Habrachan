package com.makentoshe.habrachan.view.post.comments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.post.comment.ArticleCommentEpoxyModel
import com.makentoshe.habrachan.model.post.comment.ArticleCommentsEpoxyController
import com.makentoshe.habrachan.model.post.comment.OnCommentClickListenerFactory
import com.makentoshe.habrachan.model.post.comment.SpannedFactory
import com.makentoshe.habrachan.ui.post.comments.CommentsFragmentUi
import com.makentoshe.habrachan.viewmodel.post.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFragment : Fragment() {

    val arguments = Arguments(this)
    private val disposables = CompositeDisposable()
    private val viewModel by inject<CommentsFragmentViewModel>()
    private val spannedFactory by inject<SpannedFactory>()
    private val commentClickListerFactory by inject<OnCommentClickListenerFactory>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerview = view.findViewById<RecyclerView>(R.id.article_comments_recyclerview)
        val factory = ArticleCommentEpoxyModel.Factory(spannedFactory, commentClickListerFactory)
        val epoxyController = ArticleCommentsEpoxyController(factory)

        viewModel.successObservable.subscribe {
            epoxyController.setComments(parseComments(it.data))
            recyclerview.adapter = epoxyController.adapter
            epoxyController.requestModelBuild()
        }.let(disposables::add)

        viewModel.errorObservable.subscribe {
            println(it)
        }.let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun parseComments(comments: List<Comment>): SparseArray<ArrayList<Comment>> {
        val map = SparseArray<ArrayList<Comment>>()
        comments.forEach { comment ->
            if (!map.containsKey(comment.thread)) {
                map[comment.thread] = ArrayList()
            }
            map[comment.thread]?.add(comment)
        }
        return map
    }

    class Factory {
        fun build(articleId: Int): CommentsFragment {
            return CommentsFragment().apply {
                arguments.articleId = articleId
            }
        }
    }

    class Arguments(fragment: CommentsFragment) {
        init {
            try {
                fragment.requireArguments()
            } catch (ise: IllegalStateException) {
                (fragment as Fragment).arguments = Bundle()
            }
        }

        private val fragmentArguments = fragment.requireArguments()

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID) ?: -1

        companion object {
            private const val ID = "Id"
        }
    }
}
