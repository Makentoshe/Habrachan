package com.makentoshe.habrachan.application.android.screen.comments.dispatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.comment
import com.makentoshe.habrachan.application.android.common.core.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.core.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.common.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import kotlinx.android.synthetic.main.fragment_comments_dispatch.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class DispatchCommentsFragment : BaseFragment() {

    companion object {
        fun build(articleId: ArticleId, commentId: CommentId) = DispatchCommentsFragment().apply {
            arguments.commentId = commentId
            arguments.articleId = articleId
        }
    }

    override val arguments = Arguments(this)

    private val backwardNavigator by inject<BackwardNavigator>()
    private val getArticleCommentsViewModel by inject<GetArticleCommentsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments_dispatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tintColor = ContextCompat.getColor(requireContext(), R.color.brand_dark)
        fragment_comments_dispatch_toolbar.navigationIcon?.setTint(tintColor)
        fragment_comments_dispatch_toolbar.setNavigationOnClickListener { backwardNavigator.toPreviousScreen() }

        lifecycleScope.launch(Dispatchers.IO) {
            getArticleCommentsViewModel.model.collectLatest { onGetArticleCommentsModel(it) }
        }
    }

    private fun onGetArticleCommentsModel(result: Result<GetArticleCommentsModel>) = result.fold({ model ->
        onGetArticleCommentsModelSuccess(model)
    },{ throwable ->
        onGetArticleCommentsModelFailure(throwable)
    })

    private fun onGetArticleCommentsModelSuccess(getArticleCommentsModel: GetArticleCommentsModel) {
        val commentModelNode = getArticleCommentsModel.comment(arguments.commentId) ?: return onGetArticleCommentsModelFailure(IllegalStateException())
        val commentViewController = CommentViewController(CommentViewHolder(fragment_comments_dispatch_comment))
        commentViewController.panel.showHidden()
        commentViewController.body.expand()
        commentViewController.body.level.setLevel(0)
        commentViewController.body.author.setAuthor(commentModelNode.comment)
        commentViewController.body.timestamp.setTimestamp(commentModelNode.comment)
        commentViewController.body.avatar.setStubAvatar()
        commentViewController.body.content.setContent(commentModelNode.comment)
    }

    private fun onGetArticleCommentsModelFailure(throwable: Throwable) {
        println(throwable)
        throw throwable
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