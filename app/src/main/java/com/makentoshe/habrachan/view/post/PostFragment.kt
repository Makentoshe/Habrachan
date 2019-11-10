package com.makentoshe.habrachan.view.post

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.post.PostFragmentModule
import com.makentoshe.habrachan.di.post.PostFragmentScope
import com.makentoshe.habrachan.model.post.HabrachanWebViewClient
import com.makentoshe.habrachan.model.post.JavaScriptInterface
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostFragment : Fragment() {
    private val viewModel by inject<PostFragmentViewModel>()
    private val webViewClient by inject<HabrachanWebViewClient>()
    private val javaScriptInterface by inject<JavaScriptInterface>()

    private var postId: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("id", value)
        get() = arguments?.getInt("id") ?: -1

    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PostFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val webview = view.findViewById<WebView>(R.id.post_fragment_webview)
        WebViewController(disposables, webview, javaScriptInterface, webViewClient).install(viewModel)

        val toolbar = view.findViewById<Toolbar>(R.id.post_fragment_toolbar)
        val drawable = resources.getDrawable(R.drawable.ic_arrow_back, requireContext().theme)
        ToolbarController(toolbar, drawable).install(viewModel)

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        ProgressBarController(disposables, progressBar).install(viewModel)

        viewModel.errorObservable.subscribe { throwable ->
            Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_LONG).show()
        }.let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {

        fun build(postId: Int) = PostFragment().apply {
            this.postId = postId
        }
    }

    private fun injectDependencies() {
        val module = PostFragmentModule.Builder(postId).build(this)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostFragmentScope::class.java)
        scopes.closeOnDestroy(this).installModules(module).inject(this)
        Toothpick.closeScope(scopes)
    }

    class WebViewController(
        private val disposables: CompositeDisposable,
        private val webview: WebView,
        private val javaScriptInterface: JavaScriptInterface,
        private val client: WebViewClient
    ) {

        init {
            webview.webViewClient = client
            webview.isHorizontalScrollBarEnabled = false
            webview.settings.javaScriptEnabled = true
            webview.addJavascriptInterface(javaScriptInterface, "JSInterface")
        }

        fun install(viewModel: PostFragmentViewModel) {
            viewModel.publicationObservable.subscribe { html ->
                webview.loadData(html, "text/html", "UFT-8")
            }.let(disposables::add)

            viewModel.successObservable.subscribe {
                webview.visibility = View.VISIBLE
            }.let(disposables::add)

            viewModel.errorObservable.subscribe {
                webview.visibility = View.GONE
            }.let(disposables::add)
        }
    }

    class ToolbarController(
        private val toolbar: Toolbar,
        private val navigationDrawable: Drawable
    ) {

        init {
            toolbar.navigationIcon = navigationDrawable
        }

        fun install(viewModel: PostFragmentViewModel) {
            toolbar.setNavigationOnClickListener {
                viewModel.backToMainPostsScreen()
            }
        }
    }

    class ProgressBarController(
        private val disposables: CompositeDisposable,
        private val progressBar: ProgressBar
    ) {
        fun install(viewModel: PostFragmentViewModel) {
            val success = viewModel.successObservable.map { Unit }
            val error = viewModel.errorObservable.map { Unit }
            success.mergeWith(error).subscribe {
                progressBar.visibility = View.GONE
            }.let(disposables::add)
        }
    }
}
