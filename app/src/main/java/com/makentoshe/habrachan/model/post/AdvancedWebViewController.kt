package com.makentoshe.habrachan.model.post

import android.graphics.Bitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.OnLifecycleEvent
import com.makentoshe.habrachan.view.post.ArticleFragment
import im.delight.android.webview.AdvancedWebView

class AdvancedWebViewController(private val fragment: ArticleFragment) : LifecycleObserver {

    private lateinit var advancedWebView: AdvancedWebView
    val listener: AdvancedWebView.Listener = object :
        AdvancedWebView.Listener {
        override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) = fragment.onArticleError()
        override fun onDownloadRequested(a: String?, b: String?, c: String?, d: Long, e: String?, f: String?) = Unit
        override fun onExternalPageRequest(url: String?) = Unit
        override fun onPageStarted(url: String?, favicon: Bitmap?) = Unit
        override fun onPageFinished(url: String?) = fragment.onArticleDisplayed()
    }

    fun init(advancedWebView: AdvancedWebView) {
        this.advancedWebView = advancedWebView
        LifecycleRegistry(fragment).addObserver(this)
        advancedWebView.setListener(fragment.requireActivity(), listener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        advancedWebView.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onPause() {
        advancedWebView.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onDestroy() {
        advancedWebView.onDestroy()
    }

}