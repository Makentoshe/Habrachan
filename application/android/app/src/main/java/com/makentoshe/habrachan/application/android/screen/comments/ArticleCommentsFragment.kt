package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsDataSource
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments_article.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleCommentsFragment : CoreFragment() {

    companion object {
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "ArticleCommentsFragment", message())
        }

        fun build(
            /** Comments will be downloaded for selected article. */
            articleId: Int,
            /** String that will be placed at the toolbar */
            articleTitle: String,
            /** Parent comment that may be displayed at the top. If 0 nothing will be displayed */
            commentId: Int = 0
        ) = ArticleCommentsFragment().apply {
            arguments.articleId = articleId
            arguments.articleTitle = articleTitle
            arguments.commentId = commentId
        }
    }

    override val arguments = Arguments(this)
    private val adapter by inject<CommentAdapter>()
    private val viewModel by inject<ArticleCommentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            if (savedInstanceState != null) return@launch
            val spec = CommentsDataSource.CommentsSpec(arguments.articleId, arguments.commentId)
            viewModel.sendSpecChannel.send(spec)
        }

        fragment_comments_article_toolbar.title = arguments.articleTitle
        fragment_comments_article_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_comments_article_toolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }

        fragment_comments_article_recycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.comments.collectLatest { adapter.submitData(it) }
        }

        adapter.addLoadStateListener {
            println(it)
        }
    }

    class Arguments(fragment: ArticleCommentsFragment) : CoreFragment.Arguments(fragment) {

        var articleId: Int
            get() = fragmentArguments.getInt(ARTICLE_ID)
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value)

        var articleTitle: String
            get() = fragmentArguments.getString(ARTICLE_TITLE, "")
            set(value) = fragmentArguments.putString(ARTICLE_TITLE, value)

        var commentId: Int
            get() = fragmentArguments.getInt(COMMENT_ID)
            set(value) = fragmentArguments.putInt(COMMENT_ID, value)

        companion object {
            private const val COMMENT_ID = "CommentId"
            private const val ARTICLE_ID = "ArticleId"
            private const val ARTICLE_TITLE = "ArticleTitle"
        }
    }
}
