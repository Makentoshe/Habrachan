package com.makentoshe.habrachan.view.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.post.ArticleResponse
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import com.makentoshe.habrachan.model.post.AdvancedWebViewController
import com.makentoshe.habrachan.model.post.CommentsScreen
import com.makentoshe.habrachan.model.post.html.*
import com.makentoshe.habrachan.model.post.images.PostImageScreen
import com.makentoshe.habrachan.ui.articles.PostFragmentUi
import com.makentoshe.habrachan.viewmodel.post.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.post.VoteArticleViewModel
import im.delight.android.webview.AdvancedWebView
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject

class ArticleFragment : Fragment() {

    private val advancedWebViewController by inject<AdvancedWebViewController>()
    private val articleViewModel by inject<ArticleFragmentViewModel>()
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

        articleViewModel.articleObservable.subscribe(::onArticleReceived).let(disposables::add)

//        viewModel.articleObservable.subscribe { response ->
//            when(response) {
//                is ArticleResponse.Success -> {
//                    views.webview.loadData(article.buildHtml(), "text/html", "UFT-8")
//                    views.commentsCount.text = article.commentsCount.toString()
//                    views.readingCount.text = article.readingCount.toString()
//                    views.scoreView.text = if (article.score > 0) {
//                        "+".plus(article.score.toString())
//                    } else {
//                        article.score.toString()
//                    }
//                }
//                is ArticleResponse.Error -> {
//                    views.webview.visibility = View.GONE
//                    views.progressBar.visibility = View.GONE
//                    views.retryButton.visibility = View.VISIBLE
//                    views.messageView.visibility = View.VISIBLE
//                    views.messageView.text = throwable.toString()
//                }
//            }
//        }.let(disposables::add)
//        // success
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
        view.findViewById<AdvancedWebView>(R.id.article_fragment_webview).loadHtml(response.article.buildHtml())
    }

    private fun setToolbarBehavior(response: ArticleResponse.Success) {
        val view = view ?: return onArticleError("Fragment view is null. wtf?")
        val toolbar = view.findViewById<Toolbar>(R.id.article_fragment_toolbar)
        val sas = view.findViewById<TextView>(R.id.article_fragment_calculator)
        toolbar.title = response.article.title
        sas.text = response.article.title
    }

    private fun Article.buildHtml(): String {
        val resourceRepository = RawResourceRepository(resources)
        val builder = HtmlBuilder(this)
        builder.addAddon(DisplayScriptAddon(resourceRepository))
        builder.addAddon(StyleAddon(resourceRepository))
        builder.addAddon(SpoilerAddon())
        builder.addAddon(ImageAddon())
        return builder.build()
    }

    fun onArticleError(message: String) {
        showErrorSnackbar(message)
        println(message)
    }

    fun onArticleDisplayed() = Unit

    private fun showErrorSnackbar(message: String) {
        val view = activity?.window?.decorView ?: throw Exception("Activity decor view is null")
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.got_it) { snackbar.dismiss() }
        snackbar.show()
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