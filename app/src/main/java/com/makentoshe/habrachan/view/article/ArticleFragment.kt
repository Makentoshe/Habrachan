package com.makentoshe.habrachan.view.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import com.makentoshe.habrachan.common.entity.post.ArticleResponse
import com.makentoshe.habrachan.common.ui.TextScoreController
import com.makentoshe.habrachan.common.ui.TintColorController
import com.makentoshe.habrachan.model.article.CommentsScreen
import com.makentoshe.habrachan.model.article.JavaScriptInterface
import com.makentoshe.habrachan.model.article.WebViewController
import com.makentoshe.habrachan.model.article.images.PostImageScreen
import com.makentoshe.habrachan.ui.article.CustomNestedScrollView
import com.makentoshe.habrachan.ui.article.PostFragmentUi
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import im.delight.android.webview.AdvancedWebView
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class ArticleFragment : Fragment() {

    private val navigator by inject<Navigator>()
    private val webViewController by inject<WebViewController>()
    private val getArticleViewModel by inject<ArticleFragmentViewModel>()
    private val javaScriptInterface by inject<JavaScriptInterface>()
    private val voteArticleViewModel by inject<VoteArticleViewModel>()

    private val arguments = Arguments(this)
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val request = getArticleViewModel.createRequest(arguments.articleId)
            getArticleViewModel.articleObserver.onNext(request)
        }
        view.findViewById<WebView>(R.id.article_fragment_webview).also(webViewController::init)
        view.findViewById<View>(R.id.article_fragment_retrybutton).setOnClickListener { onRetryClicked() }
        getArticleViewModel.articleObservable.subscribe(::onArticleReceived).let(disposables::add)
        javaScriptInterface.imageObservable.subscribe(navigator::toArticleResourceScreen).let(disposables::add)

        voteArticleViewModel.voteArticleObservable.subscribe(::onArticleVoted).let(disposables::add)
    }

    private fun onArticleReceived(response: ArticleResponse) = when (response) {
        is ArticleResponse.Success -> onArticleSuccess(response)
        is ArticleResponse.Error -> onArticleError(response.json)
    }

    private fun onArticleSuccess(response: ArticleResponse.Success) {
        val calculator = requireView().findViewById<TextView>(R.id.article_fragment_content_toolbar_calculator)
        calculator.text = response.article.title

        val toolbar = requireView().findViewById<Toolbar>(R.id.article_fragment_content_toolbar_toolbar)
        toolbar.title = response.article.title
        toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        val authorView = requireView().findViewById<TextView>(R.id.article_fragment_content_toolbar_author_login)
        authorView.text = response.article.author.login

        val timeView = requireView().findViewById<TextView>(R.id.article_fragment_content_toolbar_time)
        timeView.text = response.article.timePublished

        val webView = requireView().findViewById<AdvancedWebView>(R.id.article_fragment_webview)
        webView.loadHtml(response.article.buildHtml(resources))

        val voteTextView = requireView().findViewById<TextView>(R.id.article_fragment_bottombar_voteview)
        TextScoreController(voteTextView).setScore(requireContext(), response.article.score)

        val voteUpView = requireView().findViewById<View>(R.id.article_fragment_bottombar_voteup)
        voteUpView.setOnClickListener {
            voteArticleViewModel.voteUp(response.article.id)
        }

        val voteDownView = requireView().findViewById<View>(R.id.article_fragment_bottombar_votedown)
        voteDownView.setOnClickListener {
            voteArticleViewModel.voteDown(response.article.id)
        }

        if (response.article.vote == 1.0) {
            markVoteUpButton()
        }
        if (response.article.vote == -1.0) {
            markVoteDownButton()
        }

        val readingTextView = requireView().findViewById<TextView>(R.id.article_fragment_bottombar_reading_count_text)
        readingTextView.text = response.article.readingCount.toString()

        val commentsTextView = requireView().findViewById<TextView>(R.id.article_fragment_bottombar_comments_count_text)
        commentsTextView.text = response.article.commentsCount.toString()

        val commentsView = requireView().findViewById<View>(R.id.article_fragment_bottombar_comments)
        commentsView.setOnClickListener {
            navigator.toArticleCommentsScreen(arguments.articleId)
        }
    }

    fun onArticleError(message: String) {
        val messageView = requireView().findViewById<TextView>(R.id.article_fragment_messageview)
        messageView.text = message
        messageView.visibility = View.VISIBLE

        val progress = requireView().findViewById<View>(R.id.article_fragment_progressbar)
        progress.visibility = View.GONE

        val retryButton = requireView().findViewById<View>(R.id.article_fragment_retrybutton)
        retryButton.visibility = View.VISIBLE
    }

    fun onArticleDisplayed() {
        val scrollView = requireView().findViewById<CustomNestedScrollView>(R.id.article_fragment_scroll)
        scrollView.visibility = View.VISIBLE

        val containerView = requireView().findViewById<View>(R.id.article_fragment_content_toolbar_container)
        containerView.visibility = View.VISIBLE

        val appbar = requireView().findViewById<AppBarLayout>(R.id.article_fragment_content_toolbar_appbar)
        appbar.setExpanded(true, true)

        val progress = requireView().findViewById<View>(R.id.article_fragment_progressbar)
        progress.visibility = View.GONE

        val bottomBar = requireView().findViewById<View>(R.id.article_fragment_bottombar)
        bottomBar.visibility = View.VISIBLE
    }

    private fun onArticleVoted(response: VoteArticleResponse) = when (response) {
        is VoteArticleResponse.Success -> onArticleVotedSuccess(response)
        is VoteArticleResponse.Error -> onArticleVotedError(response)
    }

    private fun onArticleVotedSuccess(response: VoteArticleResponse.Success) {
        val voteTextView = requireView().findViewById<TextView>(R.id.article_fragment_bottombar_voteview)
        val currentScore = voteTextView.text.toString().toInt()
        TextScoreController(voteTextView).setScore(requireContext(), response.score)

        if (response.score > currentScore) markVoteUpButton() else markVoteDownButton()
    }

    private fun markVoteUpButton() {
        val icon = requireView().findViewById<ImageView>(R.id.article_fragment_bottombar_voteup_icon)
        TintColorController(icon).setPositiveTint(requireContext())
    }

    private fun markVoteDownButton() {
        val icon = requireView().findViewById<ImageView>(R.id.article_fragment_bottombar_votedown_icon)
        TintColorController(icon).setNegativeTint(requireContext())
    }

    private fun onArticleVotedError(response: VoteArticleResponse.Error) {
        val snackbar = Snackbar.make(requireView(), response.message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.got_it) { snackbar.dismiss() }
        if (response.code != 401) {
            snackbar.setText(response.additional.joinToString(". "))
        }
        snackbar.show()
    }

    private fun onRetryClicked() {
        val messageView = requireView().findViewById<TextView>(R.id.article_fragment_messageview)
        messageView.visibility = View.GONE

        val progress = requireView().findViewById<View>(R.id.article_fragment_progressbar)
        progress.visibility = View.VISIBLE

        val retryButton = requireView().findViewById<View>(R.id.article_fragment_retrybutton)
        retryButton.visibility = View.GONE

        val request = getArticleViewModel.createRequest(arguments.articleId)
        getArticleViewModel.articleObserver.onNext(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build(postId: Int) = ArticleFragment().apply {
            arguments.articleId = postId
        }
    }

    class Navigator(private val router: Router) {

        /** Returns to MainScreen */
        fun back() {
            router.exit()
        }

        /** Navigates to [PostImageScreen] */
        fun toArticleResourceScreen(resource: String) {
            router.navigateTo(PostImageScreen(resource))
        }

        fun toArticleCommentsScreen(articleId: Int) {
            router.navigateTo(CommentsScreen(articleId))
        }
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
            get() = fragmentArguments.getInt(ID) ?: -1

        companion object {
            private const val ID = "Id"
        }
    }

}