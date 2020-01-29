package com.makentoshe.habrachan.view.post.comments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.containsKey
import androidx.core.util.putAll
import androidx.core.util.set
import androidx.core.util.valueIterator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.ui.post.comments.CommentsFragmentUi
import com.makentoshe.habrachan.viewmodel.post.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFragment : Fragment() {

    val arguments = Arguments(this)
    private val disposables = CompositeDisposable()
    private val viewModel by inject<CommentsFragmentViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerview = view.findViewById<RecyclerView>(R.id.article_comments_recyclerview)
        val epoxyController = ArticleCommentsEpoxyController(ArticleCommentEpoxyModel.Factory())

        viewModel.successObservable.subscribe {
            epoxyController.setComments(parseComments(it.data))
            recyclerview.adapter = epoxyController.adapter
            epoxyController.requestModelBuild()

//            val maxLevel = it.data.reduce { acc, comment -> if (acc.level < comment.level) comment else acc }.level
//            val step = (view.width / 3) / maxLevel
//            println("$maxLevel\t${view.width}\t$step")
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

class ArticleCommentsEpoxyController(private val factory: ArticleCommentEpoxyModel.Factory) : EpoxyController() {

    private var comments = SparseArray<ArrayList<Comment>>() //= parseComments(commentsResponse.data)

    fun setComments(comments: SparseArray<ArrayList<Comment>>) {
        this.comments = comments
    }

    override fun buildModels() {
        comments.valueIterator().forEach { comments ->
            comments.forEach { comment ->
                factory.build(comment).addTo(this)
            }
        }
    }

    private fun parseComments(comments: List<Comment>): SparseArray<out List<Comment>> {
        val map = SparseArray<ArrayList<Comment>>()
        comments.forEach { comment ->
            if (!map.containsKey(comment.thread)) {
                map[comment.thread] = ArrayList()
            }
            map[comment.thread]?.add(comment)
        }
        return map
    }
}


@EpoxyModelClass(layout = R.layout.comments_fragment_comment)
abstract class ArticleCommentEpoxyModel : EpoxyModelWithHolder<ArticleCommentEpoxyModel.ViewHolder>() {

    @EpoxyAttribute
    var message = ""

    override fun bind(holder: ViewHolder) {
        holder.messageView?.text = message
    }

    class ViewHolder : EpoxyHolder() {
        var messageView: TextView? = null

        override fun bindView(itemView: View) {
            messageView = itemView.findViewById(R.id.sas)
        }
    }

    class Factory {
        fun build(comment: Comment): ArticleCommentEpoxyModel {
            val model = ArticleCommentEpoxyModel_()
            model.id(comment.id)
            model.message = comment.message
            return model
        }
    }
}
