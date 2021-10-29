package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.login.LoginAuth

data class GetLoginRequest(val parameters: AdditionalRequestParameters, val loginAuth: LoginAuth)