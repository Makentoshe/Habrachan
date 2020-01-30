package com.makentoshe.habrachan.view.post.comments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.core.util.valueIterator
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
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

            val maxLevel = it.data.reduce { acc, comment -> if (acc.level < comment.level) comment else acc }.level
            val step = (recyclerview.width / 2) / maxLevel
            epoxyController.setStep(step)

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

class ArticleCommentsEpoxyController(private val factory: ArticleCommentEpoxyModel.Factory) : EpoxyController() {

    private var comments = SparseArray<ArrayList<Comment>>() //= parseComments(commentsResponse.data)

    private var stepSpacePx = 0

    fun setComments(comments: SparseArray<ArrayList<Comment>>) {
        this.comments = comments
    }

    fun setStep(stepSpacePx: Int) {
        this.stepSpacePx = stepSpacePx
    }

    override fun buildModels() {
        comments.valueIterator().forEach { comments ->
            comments.forEach { comment ->
                factory.build(stepSpacePx, comment).addTo(this)
            }
        }
    }

}

@EpoxyModelClass(layout = R.layout.comments_fragment_comment)
abstract class ArticleCommentEpoxyModel : EpoxyModelWithHolder<ArticleCommentEpoxyModel.ViewHolder>() {

    @EpoxyAttribute
    var message = ""

    @EpoxyAttribute
    var author = ""

    @EpoxyAttribute
    var score = 0

    @EpoxyAttribute
    var timePublished = ""

    var stepSpacePx: Int = 0

    override fun bind(holder: ViewHolder) {
        holder.messageView?.text = message
        holder.authorView?.text = author
        holder.scoreView?.text = score.toString()
        holder.timePublishedView?.text = timePublished
        holder.rootView?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            marginStart = stepSpacePx
        }
    }

    class ViewHolder : EpoxyHolder() {
        var messageView: TextView? = null
        var authorView: TextView? = null
        var timePublishedView: TextView? = null
        var scoreView: TextView? = null
        var avatarView: ImageView? = null
        var rootView: View? = null

        override fun bindView(itemView: View) {
            messageView = itemView.findViewById(R.id.comments_fragment_comment_message)
            authorView = itemView.findViewById(R.id.comments_fragment_comment_author)
            timePublishedView = itemView.findViewById(R.id.comments_fragment_comment_timepublished)
            scoreView = itemView.findViewById(R.id.comments_fragment_comment_score)
            avatarView = itemView.findViewById(R.id.comments_fragment_comment_avatar)
            rootView = itemView
        }
    }

    class Factory {
        fun build(stepSpacePx: Int, comment: Comment): ArticleCommentEpoxyModel {
            val model = ArticleCommentEpoxyModel_()
            model.id(comment.id)
            model.message = comment.message
            model.stepSpacePx = stepSpacePx * comment.level
            model.author = comment.author.login
            model.timePublished = comment.timePublished
            model.score = comment.score
            return model
        }
    }
}
