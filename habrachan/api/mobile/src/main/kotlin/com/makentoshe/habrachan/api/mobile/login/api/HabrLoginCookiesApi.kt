package com.makentoshe.habrachan.api.mobile.login.api

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.HabrApiPath

data class HabrLoginCookiesApiBuilder(override val path: String) : HabrApiPath

data class HabrLoginCookiesApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>,
) : HabrApiGet