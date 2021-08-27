package com.makentoshe.habrachan.application.android.screen.comments.dispatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.common.core.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.core.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.common.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.entity.commentId
import kotlinx.android.synthetic.main.fragment_comments_dispatch.*
import toothpick.ktp.delegate.inject

class DispatchCommentsFragment : BaseFragment() {

    companion object {
        fun build() = DispatchCommentsFragment().apply {

        }
    }

    override val arguments = Arguments(this)

    private val backwardNavigator by inject<BackwardNavigator>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments_dispatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tintColor = ContextCompat.getColor(requireContext(), R.color.brand_dark)
        fragment_comments_dispatch_toolbar.navigationIcon?.setTint(tintColor)
        fragment_comments_dispatch_toolbar.setNavigationOnClickListener { backwardNavigator.toPreviousScreen() }
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