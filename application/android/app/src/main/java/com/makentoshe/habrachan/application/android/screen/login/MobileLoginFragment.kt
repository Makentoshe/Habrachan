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
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.WebMobileLoginViewModel
import kotlinx.android.synthetic.main.fragment_login_mobile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class MobileLoginFragment : CoreFragment() {

    companion object {
        fun build() = MobileLoginFragment()
    }

    override val arguments = Arguments(this)

    private val navigation by inject<LoginNavigation>()
    private val viewModel by inject<WebMobileLoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_mobile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_login_webview.webViewClient = CustomWebViewClient(viewModel, lifecycleScope)
        fragment_login_webview.settings.javaScriptEnabled = true
        fragment_login_webview.settings.loadWithOverviewMode = true
        fragment_login_webview.settings.useWideViewPort = true

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.webViewUrlFlow.collectLatest {
                fragment_login_webview.loadUrl(it)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.mobileLoginResponseFlow.collectLatest {
                println(it)
            }
        }
    }

    class Arguments(fragment: MobileLoginFragment) : CoreFragment.Arguments(fragment)
}

class CustomWebViewClient(
    private val viewModel: WebMobileLoginViewModel, private val coroutineScope: CoroutineScope
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url ?: throw IllegalStateException()
        if (url.path.equals("/ac/entrance/")) {
            coroutineScope.launch { viewModel.webViewLoginUrlChannel.send(url.toString()) }
            return true
        } else {
            return false
        }
    }
}