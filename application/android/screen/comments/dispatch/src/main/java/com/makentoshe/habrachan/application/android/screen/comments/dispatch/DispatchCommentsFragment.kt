package com.makentoshe.habrachan.application.android.screen.comments.dispatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.common.core.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.core.fragment.FragmentArguments
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.entity.commentId

class DispatchCommentsFragment : BaseFragment() {

    companion object {
        fun build() = DispatchCommentsFragment().apply {

        }
    }

    override val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply { text = "Dispatch comment reply" }
    }

    class Arguments(fragment: DispatchCommentsFragment) : FragmentArguments(fragment) {

        var articleId: ArticleId
            get() = articleId(fragmentArguments.getInt(ARTICLE_ID))
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value.articleId)

        var commentId: CommentId
            get() = commentId(fragmentArguments.getInt(COMMENT_ID))
            set(value) = fragmentArguments.putInt(COMMENT_ID, value.commentId)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
            private const val COMMENT_ID = "CommentId"
        }
    }
}