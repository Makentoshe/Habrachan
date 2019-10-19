package com.makentoshe.habrachan.view.post

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.post.PostFragmentModule
import com.makentoshe.habrachan.di.post.PostFragmentScope
import com.makentoshe.habrachan.model.post.HabrachanWebViewClient
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class PostFragment : Fragment() {

    private val postFragmentUi by inject<PostFragmentUi>()

    private val viewModel by inject<PostFragmentViewModel>()

    private val webViewClient by inject<HabrachanWebViewClient>()

    private val router by inject<Router>()

    private var position: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("position", value)
        get() = arguments!!.getInt("position")

    private var page: Int
        set(value) = (arguments ?: Bundle().also { arguments = it }).putInt("page", value)
        get() = arguments!!.getInt("page")

    private val disposables = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return postFragmentUi.createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.publicationObservable.subscribe { html ->
            setPublicationHtmlText(html)
        }.let(disposables::add)

        webViewClient.onPublicationReadyToShow {
            view.findViewById<View>(R.id.post_fragment_body).visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setPublicationHtmlText(html: String) {
        val webview = requireView().findViewById<WebView>(R.id.post_fragment_webview)
        webview.webViewClient = webViewClient
        webview.settings.javaScriptEnabled = true
        webview.isHorizontalScrollBarEnabled = false
        webview.loadData(html, "text/html", "UFT-8")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    companion object {
        fun build(page: Int, position: Int) = PostFragment().apply {
            this.page = page
            this.position = position
        }
    }

    private fun injectDependencies() {
        val module = PostFragmentModule.Builder(position, page).build(this)
        val scopes = Toothpick.openScopes(
            ApplicationScope::class.java, PostFragmentScope::class.java
        )
        scopes.closeOnDestroy(this)
        scopes.installModules(module)
        scopes.inject(this)
        Toothpick.closeScope(scopes)
    }
}

