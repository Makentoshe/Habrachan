@file:Suppress("FoldInitializerAndIfToElvis")

package com.makentoshe.habrachan.application.android.screen.comments.dispatch

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarSpec
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.model.GetArticleCommentsModel
import com.makentoshe.habrachan.application.android.common.comment.model.comment
import com.makentoshe.habrachan.application.android.common.comment.posting.PostCommentModel
import com.makentoshe.habrachan.application.android.common.comment.posting.PostCommentSpec
import com.makentoshe.habrachan.application.android.common.comment.posting.PostCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.GetContentResponse
import kotlinx.android.synthetic.main.fragment_comments_dispatch.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class DispatchCommentsFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(articleId: ArticleId, commentId: CommentId) = DispatchCommentsFragment().apply {
            arguments.commentId = commentId
            arguments.articleId = articleId
        }
    }

    override val arguments = Arguments(this)

    private val backwardNavigator by inject<BackwardNavigator>()
    private val getArticleCommentsViewModel by inject<GetArticleCommentsViewModel>()
    private val getArticleViewModel by inject<GetArticleViewModel>()
    private val getAvatarViewModel by inject<GetAvatarViewModel>()
    private val postCommentViewModel by inject<PostCommentViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments_dispatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener { /* a simple hack until we fixed a stacked screens issue */ }

        val tintColor = ContextCompat.getColor(requireContext(), R.color.brand_dark)
        fragment_comments_dispatch_toolbar.navigationIcon?.setTint(tintColor)
        fragment_comments_dispatch_toolbar.setNavigationOnClickListener { backwardNavigator.toPreviousScreen() }

        fragment_comments_dispatch_bottom_send.setOnClickListener { onPostCommentSendClick() }

        lifecycleScope.launch(Dispatchers.IO) {
            getArticleCommentsViewModel.model.collectLatest { onGetArticleCommentsModel(it) }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            getArticleViewModel.model.collectLatest { onGetArticleModel(it) }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            postCommentViewModel.model.collectLatest { onPostCommentModel(it) }
        }
    }

    private fun onGetArticleCommentsModel(result: Result<GetArticleCommentsModel>) = result.fold({ model ->
        onGetArticleCommentsModelSuccess(model)
    }, { throwable ->
        onGetArticleCommentsModelFailure(throwable)
    })

    private fun onGetArticleCommentsModelSuccess(getArticleCommentsModel: GetArticleCommentsModel) {
        val commentModelNode = getArticleCommentsModel.comment(arguments.commentId)
        if (commentModelNode == null) return onGetArticleCommentsModelFailure(IllegalStateException())

        val commentViewController = CommentViewController(CommentViewHolder(fragment_comments_dispatch_comment))
        commentViewController.panel.showHidden()
        commentViewController.body.expand()
        commentViewController.body.level.setLevel(0)
        commentViewController.body.author.setAuthor(commentModelNode.comment)
        commentViewController.body.timestamp.setTimestamp(commentModelNode.comment)
        commentViewController.body.content.setContent(commentModelNode.comment)

        lifecycleScope.launch(Dispatchers.IO) {
            val getAvatarSpec = commentModelNode.comment.avatar?.let(::GetAvatarSpec)
            if (getAvatarSpec == null) return@launch commentViewController.body.avatar.setStubAvatar()

            getAvatarViewModel.requestAvatar(getAvatarSpec).collectLatest { onGetAvatarModel(it) }
        }
    }

    private fun onGetAvatarModel(result: Result<GetContentResponse>) {
        result.fold({ response ->
            onGetAvatarSuccess(response)
        }, { throwable ->
            onGetAvatarFailure(throwable)
        })
    }

    private fun onGetAvatarSuccess(response: GetContentResponse) {
        val bitmap = BitmapFactory.decodeStream(response.bytes.inputStream())
        CommentViewController(CommentViewHolder(fragment_comments_dispatch_comment)).body.avatar.setAvatar(bitmap)
    }

    private fun onGetAvatarFailure(throwable: Throwable) {
        capture(analyticEvent(throwable) { "Error while loading an avatar" })
        CommentViewController(CommentViewHolder(fragment_comments_dispatch_comment)).body.avatar.setStubAvatar()
    }

    private fun onGetArticleCommentsModelFailure(throwable: Throwable) {
        println(throwable)
        throw throwable
    }

    private fun onGetArticleModel(result: Result<GetArticleModel>) {
        result.fold({ model ->
            onGetArticleModelSuccess(model)
        }, { throwable ->
            onGetArticleModelFailure(throwable)
        })
    }

    private fun onGetArticleModelSuccess(model: GetArticleModel) {
        fragment_comments_dispatch_toolbar.title = model.response2.article.title
    }

    private fun onGetArticleModelFailure(throwable: Throwable) {
        capture(analyticEvent(throwable) { "Error while loading an Article(${arguments.articleId.articleId})" })
        fragment_comments_dispatch_toolbar.setTitle(R.string.comments_dispatch_article_title_error)
    }

    private fun onPostCommentModel(result: Result<PostCommentModel>) {
        lifecycleScope.launch(Dispatchers.Main) {
            fragment_comments_dispatch_bottom_progress.visibility = View.GONE
            fragment_comments_dispatch_bottom_send.visibility = View.VISIBLE
        }

        result.fold({ model ->
            onPostCommentModelSuccess(model)
        }, { throwable ->
            onPostCommentModelFailure(throwable)
        })
    }

    private fun onPostCommentModelSuccess(model: PostCommentModel) {
        val snackbar = Snackbar.make(requireView(), R.string.comments_dispatch_comment_posting_success, Snackbar.LENGTH_SHORT)
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                backwardNavigator.toPreviousScreen()
            }
        }).show()
    }

    private fun onPostCommentModelFailure(throwable: Throwable) {
        capture(analyticEvent(throwable) { "Error while posting a comment" })
        Snackbar.make(requireView(), R.string.comments_dispatch_comment_posting_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun onPostCommentSendClick() = lifecycleScope.launch(Dispatchers.IO) {
        val message = fragment_comments_dispatch_sheet_edit_text.text.toString()
        if (message.isBlank()) return@launch

        val postCommentSpec = PostCommentSpec(arguments.articleId, message, arguments.commentId)
        capture(this@DispatchCommentsFragment.analyticEvent { "Post comment: $postCommentSpec" })
        postCommentViewModel.channel.send(postCommentSpec)

        lifecycleScope.launch(Dispatchers.Main) {
            fragment_comments_dispatch_bottom_progress.visibility = View.VISIBLE
            fragment_comments_dispatch_bottom_send.visibility = View.GONE
        }
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