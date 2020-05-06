package com.makentoshe.habrachan.view.article

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.model.article.web.JavaScriptInterface
import com.makentoshe.habrachan.model.article.web.WebViewController
import com.makentoshe.habrachan.ui.article.WebArticleFragmentUi
import toothpick.ktp.delegate.inject

class WebArticleFragment : ArticleFragment() {

    // display content events
    private val webViewController by inject<WebViewController>()

    // callbacks
    private val javaScriptInterface by inject<JavaScriptInterface>()

    private lateinit var webView: WebView
    private lateinit var bottombarView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return WebArticleFragmentUi(container).createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView = view.findViewById(R.id.article_fragment_webview)
        bottombarView = view.findViewById(R.id.article_fragment_bottombar)
        super.onViewCreated(view, savedInstanceState)
        webViewController.init(webView)
        javaScriptInterface.imageObservable.subscribe(navigator::toArticleResourceScreen).let(disposables::add)
    }

    override fun onArticleReceived(response: ArticleResponse) {
        super.onArticleReceived(response)
        if (response is ArticleResponse.Success) {
            val html = response.article.buildHtml(resources)
            val base64content = Base64.encodeToString(html.toByteArray(), Base64.DEFAULT)
            webView.loadData(base64content, "text/html; charset=UTF-8", "base64")
        }
    }

    //todo закрыть интерфейсом
    fun onArticleDisplayed() {
        bottombarView.visibility = View.VISIBLE
    }
}