package com.makentoshe.habrachan.api.android.login

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.login.LoginAuth
import com.makentoshe.habrachan.api.login.api.HabrLoginApiBuilder
import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApi
import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApiBuilder

fun HabrLoginApiBuilder.auth(auth: LoginAuth): HabrLoginAuthApiBuilder {
    return HabrLoginAuthApiBuilder(path.plus("/auth/o/access-token"), auth)
}

fun HabrLoginAuthApiBuilder.build(parameters: AdditionalRequestParameters): HabrLoginAuthApi {
    val body = auth.parameters.toList().joinToString("&") { "${it.first}=${it.second}" }
    return HabrLoginAuthApi(path, parameters.queries, parameters.headers, body)
}
