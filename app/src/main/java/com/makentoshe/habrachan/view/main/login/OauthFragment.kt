package com.makentoshe.habrachan.view.main.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import com.makentoshe.habrachan.model.main.login.OauthType
import com.makentoshe.habrachan.navigation.main.login.OauthFragmentArguments
import com.makentoshe.habrachan.ui.main.account.login.OauthFragmentUi
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class OauthFragment : Fragment() {

    private val arguments = OauthFragmentArguments(this)

    private val loginViewModel by inject<LoginViewModel>()
    private val disposables by inject<CompositeDisposable>()

    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return OauthFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView = view.findViewById(R.id.oauth_fragment_webview)
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = OAuthWebViewClient(loginViewModel)

        if (savedInstanceState == null) {
            loginViewModel.oauthRequestObserver.onNext(OauthType.Github)
        }

        loginViewModel.oauthInterimResponse.observeOn(AndroidSchedulers.mainThread())
            .subscribe(webView::loadUrl).let(disposables::add)

        loginViewModel.oauthResponseObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onOauthResponse).let(disposables::add)
    }

    private fun onOauthResponse(response: OAuthResponse) = when (response) {
        is OAuthResponse.Interim -> {
        }
        is OAuthResponse.Error -> {
            //todo handle error
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}

class OAuthWebViewClient(private val viewModel: LoginViewModel) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        println("WebView: ${request.url}")
        viewModel.oauthInterimRequestSubject.onNext(request.url.toString())
        return true

//        val response = view.tag as OAuthResponse.Interim
//        val condition = response.request.redirectUri.contains(request.url.host.toString())
//        if (condition) {
//            // shitty parsing but others does not work
//            val token = request.url.toString().split("token=")[1]
//
//            val intent = Intent().putExtra(response.request.responseType, token)
//            fragment.targetFragment?.onActivityResult(OAuthResponse::javaClass.hashCode(), Activity.RESULT_OK, intent)
//            fragment.parentFragmentManager.beginTransaction().remove(fragment).commit()
//        } else {
//            response.cookies.forEach { cookie ->
//                CookieManager.getInstance().setCookie(request.url?.toString(), cookie.toString());
//            }
//        }
//        return condition
    }
}
