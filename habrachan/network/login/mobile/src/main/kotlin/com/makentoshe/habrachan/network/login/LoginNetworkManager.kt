package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApi
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.login
import com.makentoshe.habrachan.api.mobile.login.auth
import com.makentoshe.habrachan.api.mobile.login.build

class LoginNetworkManager(private val webCallback: (HabrLoginAuthApi) -> Unit) {

    suspend fun execute(request: LoginRequest) {
        webCallback.invoke(MobileHabrApi.login().auth(request.loginAuth).build(request.parameters))
    }
}