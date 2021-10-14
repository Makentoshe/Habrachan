package com.makentoshe.habrachan.api.mobile.login

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.login.LoginAuth
import com.makentoshe.habrachan.api.login.api.HabrLoginApiBuilder
import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApi
import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApiBuilder
import com.makentoshe.habrachan.api.mobile.login.api.HabrLoginCookiesApi
import com.makentoshe.habrachan.api.mobile.login.api.HabrLoginCookiesApiBuilder

/** This auth requires a state field and Referer header from another request */
fun HabrLoginApiBuilder.auth(auth: LoginAuth): HabrLoginAuthApiBuilder {
    return HabrLoginAuthApiBuilder("https://account.habr.com/ajax/login", auth)
}

/** This auth requires a state field and Referer header from another request. See [cookies] request */
fun HabrLoginAuthApiBuilder.build(parameters: AdditionalRequestParameters): HabrLoginAuthApi {
    val body = auth.parameters.toList().joinToString("&") { "${it.first}=${it.second}" }

    if (!parameters.headers.containsKey("Referer")) {
        throw IllegalArgumentException("Request should contain Referer header")
    }

    return HabrLoginAuthApi(path, parameters.queries, parameters.headers, body)
}

/** This builder just for default login request that allows to receive cookies and other requireable stuff */
fun HabrLoginApiBuilder.cookies(): HabrLoginCookiesApiBuilder {
    return HabrLoginCookiesApiBuilder(path.plus("/kek/v1/auth/habrahabr"))
}

fun HabrLoginCookiesApiBuilder.build(parameters: AdditionalRequestParameters): HabrLoginCookiesApi {
    return HabrLoginCookiesApi(path, parameters.queries, parameters.headers)
}


