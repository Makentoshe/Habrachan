package com.makentoshe.habrachan.view.post

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
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
        initToolbar()
        initWebView()

        viewModel.publicationObservable.subscribe { html ->
            setPublicationHtmlText(html)
        }.let(disposables::add)

        viewModel.errorObservable.subscribe { throwable ->
            Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_LONG).show()
        }.let(disposables::add)

        webViewClient.onPublicationReadyToShow {
            view.findViewById<View>(R.id.post_fragment_webview).visibility = View.VISIBLE
        }

        requireView().findViewById<Toolbar>(R.id.post_fragment_toolbar).setNavigationOnClickListener {
            viewModel.backToMainPostsScreen()
        }
    }

    private fun initToolbar() {
        val view = requireView().findViewById<Toolbar>(R.id.post_fragment_toolbar)
        val drawable = resources.getDrawable(R.drawable.ic_arrow_back, requireContext().theme)
        view.navigationIcon = drawable
    }

    private fun initWebView() {
        val webview = requireView().findViewById<WebView>(R.id.post_fragment_webview)
        webview.webViewClient = webViewClient
        webview.isHorizontalScrollBarEnabled = false
        initWebViewJavascript(webview)
    }
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initWebViewJavascript(webview: WebView) {
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(javaScriptInterface, "JSInterface")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setPublicationHtmlText(html: String) {
        val webview = requireView().findViewById<WebView>(R.id.post_fragment_webview)
        webview.loadData(html, "text/html", "UFT-8")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun injectDependencies() {
        val module = PostFragmentModule.Builder(postId).build(this)
        val scopes = Toothpick.openScopes(ApplicationScope::class.java, PostFragmentScope::class.java)
        scopes.closeOnDestroy(this).installModules(module).inject(this)
        Toothpick.closeScope(scopes)
    }

    class Factory {

        fun build(postId: Int) = PostFragment().apply {
            this.postId = postId
        }
    }
}
