package com.makentoshe.habrachan.view.post

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.post.PostFragmentModule
import com.makentoshe.habrachan.di.post.PostFragmentScope
import com.makentoshe.habrachan.model.post.CommentsScreen
import com.makentoshe.habrachan.model.post.HabrachanWebViewClient
import com.makentoshe.habrachan.model.post.JavaScriptInterface
import com.makentoshe.habrachan.model.post.PostBroadcastReceiver
import com.makentoshe.habrachan.model.post.html.*
import com.makentoshe.habrachan.model.post.images.PostImageScreen
import com.makentoshe.habrachan.ui.post.BottomBarUi
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import com.makentoshe.habrachan.viewmodel.post.VoteArticleViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostFragment : Fragment() {

    private val navigator by inject<PostFragment.Navigator>()
    private val viewModel by inject<PostFragmentViewModel>()
    private val webViewClient by inject<HabrachanWebViewClient>()
    private val javaScriptInterface by inject<JavaScriptInterface>()
    private val broadcastReceiver by inject<PostBroadcastReceiver>()

    private val voteArticleViewModel by inject<VoteArticleViewModel>()

    private val arguments = Arguments(this)
    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val views = PostFragmentViews(view, this)
        // ready to display
        viewModel.getArticle.articleObservable.subscribe { article ->
            views.webview.loadData(article.buildHtml(), "text/html", "UFT-8")
            views.commentsCount.text = article.commentsCount.toString()
            views.readingCount.text = article.readingCount.toString()
            views.scoreView.text = if (article.score > 0) {
                "+".plus(article.score.toString())
            } else {
                article.score.toString()
            }
        }.let(disposables::add)
        // success
        webViewClient.onPublicationReadyToShow {
            views.webview.visibility = View.VISIBLE
            views.progressBar.visibility = View.GONE
        }
        // error
        viewModel.getArticle.errorObservable.subscribe { throwable ->
            views.webview.visibility = View.GONE
            views.progressBar.visibility = View.GONE
            views.retryButton.visibility = View.VISIBLE
            views.messageView.visibility = View.VISIBLE
            views.messageView.text = throwable.toString()
        }.let(disposables::add)
        // retry to receive an article
        views.retryButton.setOnClickListener {
            views.retryButton.visibility = View.GONE
            views.progressBar.visibility = View.VISIBLE
            views.messageView.visibility = View.GONE
            viewModel.getArticle.requestArticle()
        }
        // vote article up
        views.bottomBar.findViewById<View>(R.id.post_fragment_bottombar_voteup).setOnClickListener {
            voteArticleViewModel.voteUp(arguments.articleId)
        }
        // vote article down
        views.bottomBar.findViewById<View>(R.id.post_fragment_bottombar_votedown).setOnClickListener {
            //            viewModel.voteArticle.voteDown()
        }
        // return to previous screen
        views.toolbar.setNavigationOnClickListener {
            navigator.back()
        }
        // show article's image
        broadcastReceiver.addOnImageClickedListener { source, sources ->
            navigator.toArticleResourceScreen(source)
        }
        // show article's comments
        views.commentsGroup.setOnClickListener {
            navigator.toArticleCommentsScreen(arguments.articleId)
        }

        voteArticleViewModel.voteArticleObservable.subscribe { response ->
            when (response) {
                is VoteArticleResponse.Error -> {
                    showErrorSnackbar(response.additional.joinToString(". "))
                }
                is VoteArticleResponse.Success -> {
                    println("SAS")
                }
            }
            println(response)
        }.let(disposables::add)
    }

    private fun showErrorSnackbar(message: String) {
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.got_it) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    private fun Article.buildHtml(): String {
        val resourceRepository = RawResourceRepository(resources)
        val builder = HtmlBuilder(this)
        builder.addAddon(DisplayScriptAddon(resourceRepository))
        builder.addAddon(StyleAddon(resourceRepository))
        builder.addAddon(TitleAddon(this))
        builder.addAddon(SpoilerAddon())
        builder.addAddon(ImageAddon())
        return builder.build()
    }

    override fun onStart() {
        super.onStart()
        broadcastReceiver.registerReceiver(requireActivity())
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build(postId: Int) = PostFragment().apply {
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

    class Arguments(fragment: PostFragment) {

        init {
            (fragment as Fragment).arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID) ?: -1

        companion object {
            private const val ID = "Id"
        }
    }

    private fun injectDependencies() {
        val module = PostFragmentModule.Builder(arguments.articleId).build(this)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostFragmentScope::class.java)
        scopes.closeOnDestroy(this).installModules(module).inject(this)
        Toothpick.closeScope(scopes)
    }

    private class PostFragmentViews(view: View, fragment: PostFragment) {

        val webview = view.findViewById<WebView>(R.id.post_fragment_webview).apply {
            webViewClient = fragment.webViewClient
            isHorizontalScrollBarEnabled = false
            settings.javaScriptEnabled = true
            addJavascriptInterface(fragment.javaScriptInterface, "JSInterface")
        }

        val toolbar = view.findViewById<Toolbar>(R.id.post_fragment_toolbar).apply {
            navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back, fragment.requireContext().theme)
        }

        val bottomBar = view.findViewById<BottomAppBar>(R.id.post_fragment_bottombar).apply {
            addView(BottomBarUi(view).createView(fragment.requireContext()))
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.post_fragment_progressbar)
        val retryButton = view.findViewById<Button>(R.id.post_fragment_retrybutton)
        val messageView = view.findViewById<TextView>(R.id.post_fragment_messageview)
        val commentsGroup = view.findViewById<View>(R.id.post_fragment_bottombar_comments)
        val commentsCount = view.findViewById<TextView>(R.id.post_fragment_bottombar_comments_count_textview)
        val readingCount = view.findViewById<TextView>(R.id.post_fragment_bottombar_reading_count_textview)
        val scoreView = view.findViewById<TextView>(R.id.post_fragment_bottombar_voteview)
    }

}
