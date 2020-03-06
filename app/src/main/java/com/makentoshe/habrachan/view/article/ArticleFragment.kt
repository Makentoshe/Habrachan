package com.makentoshe.habrachan.view.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.post.ArticleResponse
import com.makentoshe.habrachan.model.article.AdvancedWebViewController
import com.makentoshe.habrachan.model.article.CommentsScreen
import com.makentoshe.habrachan.model.article.JavaScriptInterface
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

    private val advancedWebViewController by inject<AdvancedWebViewController>()
    private val articleViewModel by inject<ArticleFragmentViewModel>()
    private val navigator by inject<Navigator>()
    private val javaScriptInterface by inject<JavaScriptInterface>()
    private val voteArticleViewModel by inject<VoteArticleViewModel>()

    private val arguments = Arguments(this)
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val request = articleViewModel.createRequest(arguments.articleId)
            articleViewModel.articleObserver.onNext(request)
        }
        view.findViewById<AdvancedWebView>(R.id.article_fragment_webview).also(advancedWebViewController::init)
        view.findViewById<View>(R.id.article_fragment_retrybutton).setOnClickListener { onRetryClicked() }
        articleViewModel.articleObservable.subscribe(::onArticleReceived).let(disposables::add)
        javaScriptInterface.imageObservable.subscribe(navigator::toArticleResourceScreen).let(disposables::add)

//        webViewClient.onPublicationReadyToShow {
//            views.webview.visibility = View.VISIBLE
//            views.progressBar.visibility = View.GONE
//        }
//        // error
//        viewModel.getArticle.errorObservable.subscribe { throwable ->
//        }.let(disposables::add)
//        // retry to receive an article
//        views.retryButton.setOnClickListener {
//            views.retryButton.visibility = View.GONE
//            views.progressBar.visibility = View.VISIBLE
//            views.messageView.visibility = View.GONE
//            viewModel.getArticle.requestArticle()
//        }
//        // vote article up
//        views.bottomBar.findViewById<View>(R.id.post_fragment_bottombar_voteup).setOnClickListener {
//            voteArticleViewModel.voteUp(arguments.articleId)
//        }
//        // vote article down
//        views.bottomBar.findViewById<View>(R.id.post_fragment_bottombar_votedown).setOnClickListener {
//            voteArticleViewModel.voteDown(arguments.articleId)
//        }
//        // return to previous screen
//        views.toolbar.setNavigationOnClickListener {
//            navigator.back()
//        }
//        // show article's image
//        broadcastReceiver.addOnImageClickedListener { source, sources ->
//            navigator.toArticleResourceScreen(source)
//        }
//        // show article's comments
//        views.commentsGroup.setOnClickListener {
//            navigator.toArticleCommentsScreen(arguments.articleId)
//        }
//
//        voteArticleViewModel.voteArticleObservable.subscribe { response ->
//            when (response) {
//                is VoteArticleResponse.Error -> {
//                    showErrorSnackbar(response.additional.joinToString(". "))
//                }
//                is VoteArticleResponse.Success -> {
//                    views.scoreView.text = response.score.toString()
//                }
//            }
//            println(response)
//        }.let(disposables::add)
    }

    private fun onArticleReceived(response: ArticleResponse) = when (response) {
        is ArticleResponse.Success -> onArticleSuccess(response)
        is ArticleResponse.Error -> onArticleError(response.json)
    }

    private fun onArticleSuccess(response: ArticleResponse.Success) {
        val view = view ?: return onArticleError("Fragment view is null. wtf?")
        setToolbarBehavior(response)
        val webView = view.findViewById<AdvancedWebView>(R.id.article_fragment_webview)
        webView.addJavascriptInterface(javaScriptInterface, "JSInterface")
        webView.loadHtml(response.article.buildHtml(resources))
    }

    private fun setToolbarBehavior(response: ArticleResponse.Success) {
        val view = view ?: return onArticleError("Fragment view is null. wtf?")
        val calculator = view.findViewById<TextView>(R.id.article_fragment_content_toolbar_calculator)
        calculator.text = response.article.title
        val toolbar = view.findViewById<Toolbar>(R.id.article_fragment_content_toolbar_toolbar)
        toolbar.title = response.article.title
        toolbar.setNavigationOnClickListener { navigator.back() }
        val authorView = view.findViewById<TextView>(R.id.article_fragment_content_toolbar_author_login)
        authorView.text = response.article.author.login
        val timeView = view.findViewById<TextView>(R.id.article_fragment_content_toolbar_time)
        timeView.text = response.article.timePublished
    }

    fun onArticleError(message: String) {
        val view = view ?: activity?.window?.decorView ?: throw Exception("Activity decor view is null")
        val messageView = view.findViewById<TextView>(R.id.article_fragment_messageview)
        messageView.text = message
        messageView.visibility = View.VISIBLE
        val progress = view.findViewById<View>(R.id.article_fragment_progressbar)
        progress.visibility = View.GONE
        val retryButton = view.findViewById<View>(R.id.article_fragment_retrybutton)
        retryButton.visibility = View.VISIBLE
    }

    fun onArticleDisplayed() {
        val view = view ?: return onArticleError("Fragment view is null. wtf?")
        val scrollView = view.findViewById<CustomNestedScrollView>(R.id.article_fragment_scroll)
        scrollView.visibility = View.VISIBLE
        val progress = view.findViewById<View>(R.id.article_fragment_progressbar)
        progress.visibility = View.GONE
        val appbar = view.findViewById<AppBarLayout>(R.id.article_fragment_content_toolbar_appbar)
        val containerView = view.findViewById<View>(R.id.article_fragment_content_toolbar_container)
        containerView.visibility = View.VISIBLE
        appbar.setExpanded(true, true)
    }

    private fun onRetryClicked() {
        val view = view ?: return onArticleError("Fragment view is null. wtf?")
        val messageView = view.findViewById<TextView>(R.id.article_fragment_messageview)
        messageView.visibility = View.GONE
        val progress = view.findViewById<View>(R.id.article_fragment_progressbar)
        progress.visibility = View.VISIBLE
        val retryButton = view.findViewById<View>(R.id.article_fragment_retrybutton)
        retryButton.visibility = View.GONE

        val request = articleViewModel.createRequest(arguments.articleId)
        articleViewModel.articleObserver.onNext(request)
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