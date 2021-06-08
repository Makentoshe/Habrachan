package com.makentoshe.habrachan.application.android.screen.article

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.screen.article.model.*
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel2
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.exception.NativeVoteArticleException
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import com.makentoshe.habrachan.network.response.GetContentResponse
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleFragment : CoreFragment(), HabrachanWebViewClientListener {

    companion object : Analytics(LogAnalytic()) {

        fun build(articleId: Int) = ArticleFragment().apply {
            arguments.articleId = articleId
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)
    private val webViewClient = HabrachanWebViewClient(this)

    private val viewModel2 by inject<ArticleViewModel2>()
    private val navigator by inject<ArticleNavigation>()

    private val exceptionHandler by inject<ExceptionHandler>()
    private lateinit var exceptionController: ExceptionController

    private val articleShareController by inject<ArticleShareController>()
    private val articleHtmlController by inject<ArticleHtmlController>()
    private val javaScriptInterface by inject<JavaScriptInterface>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exceptionController = ExceptionController(ExceptionViewHolder(fragment_article_exception))

        val wasViewModelRecreated = viewModel2.toString() != savedInstanceState?.getString(VIEW_MODEL_STATE_KEY)
        if (savedInstanceState == null || wasViewModelRecreated) lifecycleScope.launch {
            capture(analyticEvent(this@ArticleFragment.javaClass.simpleName, arguments.articleId.toString()))
            viewModel2.articleSpecChannel.send(ArticleViewModel2.ArticleSpec(arguments.articleId))
        }
        lifecycleScope.launch {
            viewModel2.avatar.collectLatest { response ->
                response.fold(::onAvatarReceivedSuccess, ::onAvatarReceivedFailure)
            }
        }
        lifecycleScope.launch {
            viewModel2.article.collectLatest { response ->
                response.fold(::onArticleReceivedSuccess, ::onArticleReceivedFailure)
            }
        }
        lifecycleScope.launch {
            javaScriptInterface.imageSourceChannel.receiveAsFlow().collectLatest {
                navigator.toArticleContentScreen(it)
            }
        }
        lifecycleScope.launch {
            viewModel2.voteArticle.collectLatest { response ->
                response.fold(::onVoteArticleSuccess, ::onVoteArticleFailure)
            }
        }

        exceptionController.setRetryButton {
            exceptionController.hide()
            fragment_article_progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel2.articleSpecChannel.send(ArticleViewModel2.ArticleSpec(arguments.articleId))
            }
        }

        fragment_article_webview.webViewClient = webViewClient
        fragment_article_webview.settings.javaScriptEnabled = true
        fragment_article_webview.addJavascriptInterface(javaScriptInterface, "JSInterface")

        fragment_article_toolbar.overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_overflow)
        fragment_article_toolbar.inflateMenu(R.menu.menu_article_overflow)
        fragment_article_toolbar.setOnMenuItemClickListener(::onOverflowMenuItemClick)

        fragment_article_bottom_voteup.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val spec = ArticleViewModel2.VoteArticleSpec(articleId(arguments.articleId), ArticleVote.Up)
                viewModel2.voteArticleSpecChannel.send(spec)
            }
        }

        fragment_article_bottom_votedown.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                ArticleVoteDownReasonDialogFragment.show(childFragmentManager)
            }
        }

        @Suppress("USELESS_CAST")
        val fragmentManager = childFragmentManager as FragmentManager
        fragmentManager.setFragmentResultListener(ArticleVoteDownReasonDialogFragment.request, this) { _, result ->
            val reason = result.getSerializable(ArticleVoteDownReasonDialogFragment.key) as ArticleVote.Down.Reason
            val spec = ArticleViewModel2.VoteArticleSpec(articleId(arguments.articleId), ArticleVote.Down(reason))
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel2.voteArticleSpecChannel.send(spec)
            }
        }
    }

    // TODO make separate class for creating html urls
    private fun onOverflowMenuItemClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_share -> articleShareController.share(requireActivity())
        else -> false
    }

    // TODO disable appbar collapse on error
    private fun onArticleReceivedSuccess(response: GetArticleResponse2) {
        onArticleReceivedToolbar(response)
        onArticleReceivedBottomBar(response)

        fragment_article_progress.visibility = View.GONE
        fragment_article_scroll.visibility = View.VISIBLE

        try {
            val articleHtml = articleHtmlController.render(response.article)
            val base64content = Base64.encodeToString(articleHtml.toByteArray(), Base64.DEFAULT)
            fragment_article_webview.loadData(base64content, "text/html; charset=UTF-8", "base64")
        } catch (e: Exception) {
            exceptionController.render(exceptionHandler.handleException(e))
            fragment_article_appbar.isEnabled = false
        }

        fragment_article_author.setOnClickListener {
            navigator.navigateToUserScreen(response.article.author.login)
        }
    }

    private fun onArticleReceivedToolbar(response: GetArticleResponse2) {
        fragment_article_toolbar.setNavigationOnClickListener { navigator.back() }
        fragment_article_toolbar.title = response.article.title
        fragment_article_calculator.text = response.article.title
        fragment_article_login.text = response.article.author.login
        fragment_article_appbar.setExpanded(true, true)
    }

    private fun onArticleReceivedBottomBar(response: GetArticleResponse2) {
        fragment_article_bottom_voteview.text = response.article.score.toString()
        fragment_article_bottom_reading_count.text = response.article.readingCount.toString()
        fragment_article_bottom_comments_count.text = response.article.commentsCount.toString()
        fragment_article_bottom.visibility = View.VISIBLE
        fragment_article_bottom_comments.setOnClickListener {
            navigator.toArticleCommentsScreen(response.article)
        }

        when (response.article.vote.value) {
            1.0 -> setVoteUpIcon()
            -1.0 -> setVoteDownIcon()
        }
    }

    private fun onArticleReceivedFailure(exception: Throwable) {
        fragment_article_progress.visibility = View.GONE
        exceptionController.render(exceptionHandler.handleException(exception))
    }

    private fun onAvatarReceivedSuccess(response: GetContentResponse) {
        val bitmap = response.bytes.toRoundedDrawable(resources, dp2px(R.dimen.radiusS))
        fragment_article_avatar_image.setImageDrawable(bitmap)
        fragment_article_avatar_image.visibility = View.VISIBLE

        fragment_article_avatar_progress.visibility = View.GONE
    }

    private fun onAvatarReceivedFailure(throwable: Throwable?) {
        val tintColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.title))
        fragment_article_avatar_image.imageTintList = tintColor
        fragment_article_avatar_image.imageTintMode = PorterDuff.Mode.SRC_ATOP
        fragment_article_avatar_image.setImageResource(R.drawable.ic_account_stub)
        fragment_article_avatar_image.visibility = View.VISIBLE

        fragment_article_avatar_progress.visibility = View.GONE
    }

    private fun onVoteArticleSuccess(response: VoteArticleResponse) {
        fragment_article_bottom_voteview.text = response.score.toString()
        when (response.request.articleVote) {
            is ArticleVote.Up -> setVoteUpIcon()
            is ArticleVote.Down -> setVoteDownIcon()
        }
    }

    private fun setVoteUpIcon() = fragment_article_bottom_voteup.apply {
        setColorFilter(ContextCompat.getColor(requireContext(), R.color.positive))
    }.setImageResource(R.drawable.ic_arrow_bold_solid)

    private fun setVoteDownIcon() = fragment_article_bottom_votedown.apply {
        setColorFilter(ContextCompat.getColor(requireContext(), R.color.negative))
    }.setImageResource(R.drawable.ic_arrow_bold_solid)

    // todo show a snackbar with description
    private fun onVoteArticleFailure(throwable: Throwable?) {
        if (throwable !is NativeVoteArticleException) return

        if (throwable.code == 401) {
            showSnackbar(
                R.string.article_vote_action_login_require_text, R.string.article_vote_action_login_require_button
            ) {
                navigator.navigateToLoginScreen()
            }
        }

        if (throwable.code == 400 && throwable.request.articleVote is ArticleVote.Down) {
            showSnackbar(
                R.string.article_vote_action_negative_describe_text,
                R.string.article_vote_action_negative_describe_button
            )
        }
    }

    private fun showSnackbar(
        @StringRes
        stringText: Int,
        @StringRes
        actionString: Int, action: (View) -> Unit = {}
    ) {
        Snackbar.make(requireView(), stringText, Snackbar.LENGTH_LONG).setAction(actionString, action).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_MODEL_STATE_KEY, viewModel2.toString())
    }

    override fun onWebReceivedError(
        view: WebView?, errorCode: Int, description: String?, failingUrl: String?
    ) {
        onArticleReceivedFailure(Exception(description))
    }

    class Arguments(articleFragment: ArticleFragment) : CoreFragment.Arguments(articleFragment) {

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID, -1)

        companion object {
            private const val ID = "ArticleId"
        }
    }
}
