package com.makentoshe.habrachan.application.android.screen.article

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.article.model.HabrachanWebViewClient
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.UserAvatarViewModel
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.VoteArticleResponse
import com.makentoshe.habrachan.common.ui.ImageTintController
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.common.ui.TextScoreController
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_article_bottombar.*
import kotlinx.android.synthetic.main.fragment_article_content.*
import kotlinx.android.synthetic.main.fragment_article_toolbar.*
import kotlinx.android.synthetic.main.fragment_article_web.*
import toothpick.ktp.delegate.inject

class ArticleFragment : Fragment() {

    companion object {

        fun build(articleId: Int) = ArticleFragment().apply {
            arguments.articleId = articleId
        }

        fun build(article: Article) = ArticleFragment().apply {
            arguments.articleId = article.id
            arguments.article = article
        }
    }

    val arguments = Arguments(this)

    private val disposables by inject<CompositeDisposable>()
    private val navigator by inject<ArticleNavigation>()

    private val articleViewModel by inject<ArticleViewModel>()
    private val userAvatarViewModel by inject<UserAvatarViewModel>()
    private val voteArticleViewModel by inject<VoteArticleViewModel>()

    private val javaScriptInterface by inject<JavaScriptInterface>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val request = articleViewModel.createRequest(arguments.articleId)
            articleViewModel.articleObserver.onNext(request)
        }

        fragment_article_webview.webViewClient = HabrachanWebViewClient(this)
        fragment_article_webview.settings.javaScriptEnabled = true
        fragment_article_webview.addJavascriptInterface(javaScriptInterface, "JSInterface")

        fragment_article_retry.setOnClickListener { onRetryClicked() }
        articleViewModel.articleObservable.subscribe(::onArticleReceived).let(disposables::add)
        userAvatarViewModel.avatarObservable.subscribe(::onAvatarReceived).let(disposables::add)
        voteArticleViewModel.voteArticleObservable.subscribe(::onArticleVoted).let(disposables::add)
        javaScriptInterface.imageObservable.subscribe{
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }.let(disposables::add)
    }

    private fun onArticleReceived(response: ArticleResponse) = when (response) {
        is ArticleResponse.Success -> onArticleReceivedSuccess(response)
        is ArticleResponse.Error -> onArticleReceivedError(response.json)
    }

    private fun onArticleReceivedSuccess(response: ArticleResponse.Success) {
        onArticleReceivedSuccessToolbar(response)
        onArticleReceivedSuccessBottombar(response)
        fragment_article_scroll.visibility = View.VISIBLE
        fragment_article_toolbar_container.visibility = View.VISIBLE
        fragment_article_progress.visibility = View.GONE

        val html = response.article.buildHtml(resources)
        val base64content = Base64.encodeToString(html.toByteArray(), Base64.DEFAULT)
        fragment_article_webview.loadData(base64content, "text/html; charset=UTF-8", "base64")
    }

    private fun onArticleReceivedSuccessToolbar(response: ArticleResponse.Success) {
        fragment_article_calculator.text = response.article.title
        fragment_article_toolbar.title = response.article.title
        fragment_article_toolbar.setNavigationOnClickListener { navigator.back() }
        fragment_article_login.text = response.article.author.login
        fragment_article_published.text = response.article.timePublished
        fragment_article_appbar.setExpanded(true, true)
        fragment_article_toolbar_container.visibility = View.VISIBLE
        fragment_article_author.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
//            TODO("Not implemented")
//            val success = navigator.toUserScreen(response.article.author.login)
//            if (!success) displaySnackbarError(resources.getString(R.string.should_be_logged_in_for_user))
        }
    }

    private fun onArticleReceivedSuccessBottombar(response: ArticleResponse.Success) {
        fragment_article_bottombar_reading_count_text.text = response.article.readingCount.toString()
        fragment_article_bottombar_comments_count_text.text = response.article.commentsCount.toString()
        TextScoreController(fragment_article_bottombar_voteview).setScoreLight(requireContext(), response.article.score)
        fragment_article_bottombar_voteup.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
//            voteArticleViewModel.voteUp(response.article.id)
        }
        if (response.article.vote == 1.0) {
            markVoteUpButton()
        }
        fragment_article_bottombar_votedown.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
//            voteArticleViewModel.voteDown(response.article.id)
        }
        if (response.article.vote == -1.0) {
            markVoteDownButton()
        }
        fragment_article_bottombar_comments.setOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
//            navigator.toArticleCommentsScreen(response.article)
        }
    }

    fun onArticleReceivedError(message: String) {
        fragment_article_message.text = message
        fragment_article_message.visibility = View.VISIBLE
        fragment_article_progress.visibility = View.GONE
        fragment_article_retry.visibility = View.VISIBLE
    }

    private fun onArticleVoted(response: VoteArticleResponse) = when (response) {
        is VoteArticleResponse.Success -> onArticleVotedSuccess(response)
        is VoteArticleResponse.Error -> onArticleVotedError(response)
    }

    private fun onArticleVotedSuccess(response: VoteArticleResponse.Success) {
        val currentScore = fragment_article_bottombar_voteview.text.toString().toInt()
        TextScoreController(fragment_article_bottombar_voteview).setScoreLight(requireContext(), response.score)
        if (response.score > currentScore) markVoteUpButton() else markVoteDownButton()
    }

    private fun markVoteUpButton() {
        ImageTintController.from(fragment_article_bottombar_voteup_icon).setPositiveTint(requireContext())
    }

    private fun markVoteDownButton() {
        ImageTintController.from(fragment_article_bottombar_votedown_icon).setNegativeTint(requireContext())
    }

    private fun onArticleVotedError(response: VoteArticleResponse.Error) {
        val message = if (response.code != 401) {
            response.additional.joinToString(". ")
        } else {
            response.message
        }
        displaySnackbarError(message)
    }

    private fun displaySnackbarError(message: String) {
        SnackbarErrorController.from(requireView()).displayIndefiniteMessage(message)
    }

    private fun onRetryClicked() {
        fragment_article_message.visibility = View.GONE
        fragment_article_progress.visibility = View.VISIBLE
        fragment_article_retry.visibility = View.GONE

        val request = articleViewModel.createRequest(arguments.articleId)
        articleViewModel.articleObserver.onNext(request)
    }

    private fun onAvatarReceived(response: ImageResponse) = when (response) {
        is ImageResponse.Success -> onAvatarSuccess(response)
        is ImageResponse.Error -> onAvatarError()
    }

    private fun onAvatarSuccess(response: ImageResponse.Success) {
        if (response.isStub) return
        ImageViewController.from(fragment_article_avatar).setAvatarFromByteArray(response.bytes)
        ImageTintController.from(fragment_article_avatar).clear()
    }

    private fun onAvatarError() {
        ImageViewController.from(fragment_article_avatar).setAvatarStub()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    //todo закрыть интерфейсом
    fun onArticleDisplayed() {
        fragment_article_bottombar.visibility = View.VISIBLE
    }

    class Arguments(private val articleFragment: ArticleFragment) {

        init {
            val fragment = articleFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = articleFragment.requireArguments()

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID, -1)

        var article: Article?
            set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
            get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

        companion object {
            private const val ID = "Id"
            private const val ARTICLE = "Article"
        }
    }
}

