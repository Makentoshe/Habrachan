package com.makentoshe.habrachan.application.android.screen.login.model

import android.content.Context
import android.webkit.*
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieModel
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieSpec
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetCookieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpCookie

class LoginWebViewClient(
    private val context: Context,
    private val cookieModel: GetCookieModel.Request,
    private val coroutineScope: CoroutineScope,
    private val cookieViewModel: GetCookieViewModel,
) : WebViewClient() {

    companion object : Analytics(LogAnalytic())

    private val cookieManager = CookieManager.getInstance()

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        // might be null, for example after captcha, so just allow this request and catch it on the next one.
        val responseCookie = cookieManager.getCookie(request.url.toString())
        if (responseCookie == null) {
            capture(analyticEvent { "Should override url: false. ${request.url}" })
            return false
        }

        val cookies = HttpCookie.parse(responseCookie)
        return cookies.filter { it.name == "habrsession_id" }.onEach { cookie ->
            coroutineScope.launch(Dispatchers.IO) {
                cookieViewModel.loginChannel.send(GetCookieSpec.Login(listOf(cookie)))
            }
        }.any().also { capture(analyticEvent { "Should override url: $it. ${request.url}" }) }
    }

    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        return when (request.url.host) {
            "account.habr.com" -> loadHabrAccountResources(request)
            else -> null
        }.also { capture(analyticEvent { "Should intercept url: ${it != null}. ${request.url}" }) }
    }

    private fun loadHabrAccountResources(request: WebResourceRequest): WebResourceResponse? {
        return when (request.url.path) {
            "/login/" -> loadLoginHabrAccountResource(request)
            "/css/1631696504/main.css" -> loadCssHabrAccountResource(request)
            else -> null
        }
    }

    private fun loadLoginHabrAccountResource(request: WebResourceRequest): WebResourceResponse {
        val templateLoginHtml = context.assets.open("login.html").readBytes().decodeToString()
        val generatedLoginHtml = templateLoginHtml.replace("e18ba9264452acd66817ee637b2ee553", cookieModel.state)

        val mimeType = context.contentResolver.getType(request.url)
        val dataStream = generatedLoginHtml.byteInputStream()
        return WebResourceResponse(mimeType, "utf-8", dataStream)
    }

    private fun loadCssHabrAccountResource(request: WebResourceRequest): WebResourceResponse {
        val templateCss = context.assets.open("main.css")
        return WebResourceResponse(context.contentResolver.getType(request.url), "utf-8", templateCss)
    }
}