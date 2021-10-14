package com.makentoshe.habrachan.api.login.api

import com.makentoshe.habrachan.api.HabrApiPath
import com.makentoshe.habrachan.api.HabrApiPost
import com.makentoshe.habrachan.api.login.LoginAuth

data class HabrLoginAuthApiBuilder(override val path: String, val auth: LoginAuth) : HabrApiPath

data class HabrLoginAuthApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>,
    override val body: String,
) : HabrApiPost