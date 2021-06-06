package com.makentoshe.habrachan.application.android.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginNavigation
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.NativeLoginViewModel
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.WebMobileLoginManager
import kotlinx.android.synthetic.main.fragment_login_mobile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class MobileLoginFragment : CoreFragment() {

    companion object {
        fun build() = MobileLoginFragment()
    }

    override val arguments = Arguments(this)

    private val navigation by inject<LoginNavigation>()
    private val viewModel by inject<NativeLoginViewModel>()
    private val webMobileLoginManager by inject<WebMobileLoginManager>()
    private val userSession by inject<UserSession>()

    private val channel = Channel<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_mobile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_login_webview.webViewClient = CustomWebViewClient(channel, lifecycleScope)
        fragment_login_webview.settings.javaScriptEnabled = true
        fragment_login_webview.settings.loadWithOverviewMode = true
        fragment_login_webview.settings.useWideViewPort = true

        lifecycleScope.launch(Dispatchers.IO) {
            val request = webMobileLoginManager.request(userSession) {
                lifecycleScope.launch(Dispatchers.Main) {
                    fragment_login_webview.loadUrl(it)
                }
                return@request channel.receive()
            }
            val response = webMobileLoginManager.login(request)
            println(response)
        }
    }

    class Arguments(fragment: MobileLoginFragment) : CoreFragment.Arguments(fragment)
}

class CustomWebViewClient(
    private val channel: Channel<String>, private val coroutineScope: CoroutineScope
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url ?: throw IllegalStateException()
        if (url.path.equals("/ac/entrance/")) {
            coroutineScope.launch { channel.send(url.toString()) }
            return true
        } else {
            return false
        }
    }
}