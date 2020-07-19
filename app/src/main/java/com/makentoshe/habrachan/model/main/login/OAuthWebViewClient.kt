package com.makentoshe.habrachan.model.main.login

import android.app.Activity
import android.content.Intent
import android.webkit.*
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import com.makentoshe.habrachan.view.main.login.OauthFragment

class OAuthWebViewClient(
    private val fragment: OauthFragment,
    private val onErrorHandler: (String) -> Unit
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val response = view.tag as OAuthResponse.Interim
        val condition = response.request.redirectUri.contains(request.url.host.toString())
        if (condition) {
            // shitty parsing but others does not work
            val token = request.url.toString().split("token=")[1]

            val intent = Intent().putExtra(response.request.responseType, token)
            fragment.targetFragment?.onActivityResult(
                OAuthResponse::javaClass.hashCode(),
                Activity.RESULT_OK, intent)
            fragment.parentFragmentManager.beginTransaction().remove(fragment).commit()
        } else {
            response.cookies.forEach { cookie ->
                CookieManager.getInstance().setCookie(request.url?.toString(), cookie.toString())
            }
        }
        return condition
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        onErrorHandler.invoke(errorResponse?.reasonPhrase.toString())
    }
}