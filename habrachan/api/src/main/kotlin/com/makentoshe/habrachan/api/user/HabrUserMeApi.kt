package com.makentoshe.habrachan.api.user

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.HabrApiPath

data class HabrUserMeApiBuilder(override val path: String) : HabrApiPath

data class HabrUserMeApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>,
) : HabrApiGet