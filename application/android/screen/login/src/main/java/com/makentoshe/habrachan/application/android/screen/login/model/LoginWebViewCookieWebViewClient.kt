package com.makentoshe.habrachan.application.android.screen.login.model

import android.content.Context
import android.webkit.*
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.usersession.HabrSessionIdCookie
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetSessionCookieViewModelRequest
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.CookieParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.net.UnknownHostException
import javax.inject.Inject

class LoginWebViewCookieWebViewClient(
    private val cookieManager: CookieManager,
    private val coroutineScope: CoroutineScope,
    private val cookieViewModel: GetCookieViewModel,
    private val newState: String,
    private val cookieParser: CookieParser,
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        // might be null, for example after captcha, so just allow this request and catch it on the next one.
        val responseCookie = cookieManager.getCookie(request.url.toString()) ?: return false
        val parsedResponseCookie = cookieParser.parseHeader(responseCookie)

        val habrSessionIdCookie = parsedResponseCookie.find {
            it.name == HabrSessionIdCookie.NAME
        }?.value?.let(::HabrSessionIdCookie)

        return shouldOverrideUrlLoadingHabrSessionId(habrSessionIdCookie)
    }

    private fun shouldOverrideUrlLoadingHabrSessionId(habrSessionIdCookie: HabrSessionIdCookie?): Boolean {
        if (habrSessionIdCookie == null) return false

        coroutineScope.launch(Dispatchers.IO) {
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

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        val exceptionEntry = when(errorCode){
            -2 -> exceptionEntry(view.context, UnknownHostException(URL(failingUrl).host))
            else -> exceptionEntry(view.context, Exception(description))
        }

        coroutineScope.launch(Dispatchers.IO) {
            cookieViewModel.connectCookieChannel.send(Either2.Right(exceptionEntry))
        }
    }

    class Factory @Inject constructor(
        private val cookieManager: CookieManager,
        private val coroutineScope: CoroutineScope,
        private val cookieViewModel: GetCookieViewModel,
        private val parser: CookieParser,
    ) {
        fun build(newState: String): LoginWebViewCookieWebViewClient {
            return LoginWebViewCookieWebViewClient(cookieManager, coroutineScope, cookieViewModel, newState, parser)
        }
    }

    companion object : Analytics(LogAnalytic())
}