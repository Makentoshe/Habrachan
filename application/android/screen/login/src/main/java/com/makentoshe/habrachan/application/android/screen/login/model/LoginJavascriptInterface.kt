package com.makentoshe.habrachan.application.android.screen.login.model

import android.webkit.JavascriptInterface
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetLoginSpec
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.GetLoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginJavascriptInterface(
    private val coroutineScope: CoroutineScope,
    private val loginViewModel: GetLoginViewModel
) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun performLogin(login: String, password: String) = coroutineScope.launch(Dispatchers.IO) {
        loginViewModel.channel.send(GetLoginSpec(Email(login), Password(password)))
    }
}