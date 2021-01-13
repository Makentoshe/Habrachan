package com.makentoshe.habrachan.application.android.screen.comments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.CoreFragment

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = TextView(requireContext()).apply { text = "Anus" }

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