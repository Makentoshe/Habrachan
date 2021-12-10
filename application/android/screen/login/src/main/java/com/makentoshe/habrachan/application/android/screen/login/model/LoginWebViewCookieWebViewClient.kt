package com.makentoshe.habrachan.application.android.screen.login.model

import android.content.Context
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetSessionCookieViewModelRequest
import com.makentoshe.habrachan.network.Cookie
import com.makentoshe.habrachan.network.CookieParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginWebViewCookieWebViewClient(
    cookieManager: CookieManager,
    cookieParser: CookieParser,
    private val cookieViewModel: GetCookieViewModel,
    private val newState: String,
) : CookieWebViewClient(cookieManager, cookieParser, cookieViewModel) {

    override fun shouldOverrideUrlLoadingParsedCookies(request: WebResourceRequest, cookies: List<Cookie>): Boolean {
        val habrSessionIdCookie = cookies.findHabrSessionIdCookie()
        val connectSidCookie = cookies.findConnectSidCookie()
        if (habrSessionIdCookie == null || connectSidCookie == null) return false

        cookieViewModel.viewModelScope.launch(Dispatchers.IO) {
            cookieViewModel.sessionCookieChannel.send(GetSessionCookieViewModelRequest(habrSessionIdCookie))
        }

        return true
    }

    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        return when (request.url.host) {
            "account.habr.com" -> loadHabrAccountResources(view.context, request)
            else -> null
        }
    }

    private fun loadHabrAccountResources(context: Context, request: WebResourceRequest): WebResourceResponse? {
        return when (request.url.path) {
            "/login/" -> loadLoginHabrAccountResource(context, request)
            "/css/1631696504/main.css" -> loadCssHabrAccountResource(context, request)
            else -> null
        }
    }

    private fun loadLoginHabrAccountResource(context: Context, request: WebResourceRequest): WebResourceResponse {
        val templateLoginHtml = context.assets.open("login.html").readBytes().decodeToString()
        val generatedLoginHtml = templateLoginHtml.replace("e18ba9264452acd66817ee637b2ee553", newState)

        val mimeType = context.contentResolver.getType(request.url)
        val dataStream = generatedLoginHtml.byteInputStream()
        return WebResourceResponse(mimeType, "utf-8", dataStream)
    }

    private fun loadCssHabrAccountResource(context: Context, request: WebResourceRequest): WebResourceResponse {
        val templateCss = context.assets.open("main.css")
        return WebResourceResponse(context.contentResolver.getType(request.url), "utf-8", templateCss)
    }

    class Factory @Inject constructor(
        private val cookieManager: CookieManager,
        private val cookieViewModel: GetCookieViewModel,
        private val parser: CookieParser,
    ) {
        fun build(newState: String): LoginWebViewCookieWebViewClient {
            return LoginWebViewCookieWebViewClient(cookieManager, parser, cookieViewModel, newState)
        }
    }

    companion object : Analytics(LogAnalytic())
}