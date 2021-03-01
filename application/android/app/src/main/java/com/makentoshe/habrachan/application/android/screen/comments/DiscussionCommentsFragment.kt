package com.makentoshe.habrachan.application.android.screen.comments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsAdapterQualifier
import com.makentoshe.habrachan.application.android.screen.comments.di.TitleAdapterQualifier
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsSpec
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.DiscussionCommentSeparatorItemDecoration
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments_discussion.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject


class DiscussionCommentsFragment : CoreFragment() {

    companion object {
        @SuppressLint("LongLogTag")
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "DiscussionCommentsFragment", message())
        }

        fun build(
            /** Comments will be downloaded for selected article. */
            articleId: Int,
            /** String that will be placed at the toolbar */
            articleTitle: String,
            /** Parent comment that may be displayed at the top. If 0 nothing will be displayed */
            commentId: Int = 0
        ) = DiscussionCommentsFragment().apply {
            arguments.articleId = articleId
            arguments.articleTitle = articleTitle
            arguments.commentId = commentId
        }
    }

    override val arguments = Arguments(this)

    private val commentAdapter by inject<CommentAdapter>(CommentsAdapterQualifier)
    private val titleAdapter by inject<CommentAdapter>(TitleAdapterQualifier)
    private val adapter by inject<ConcatAdapter>()

    private val navigation by inject<CommentsNavigation>()
    private val viewModel by inject<DiscussionCommentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_comments_discussion, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) lifecycleScope.launch(Dispatchers.IO) {
            viewModel.sendSpecChannel.send(CommentsSpec(arguments.articleId, arguments.commentId))
        }

        fragment_comments_discussion_toolbar.title = arguments.articleTitle
        fragment_comments_discussion_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_comments_discussion_toolbar.setNavigationOnClickListener { navigation.back() }

        val separateDecoration = DiscussionCommentSeparatorItemDecoration(requireContext())
        fragment_comments_discussion_recycler.addItemDecoration(separateDecoration)

        fragment_comments_discussion_recycler.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.comments.collectLatest { commentAdapter.submitData(it) }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.comment.collectLatest { titleAdapter.submitData(it) }
        }
    }

    class Arguments(fragment: DiscussionCommentsFragment) : CoreFragment.Arguments(fragment) {

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