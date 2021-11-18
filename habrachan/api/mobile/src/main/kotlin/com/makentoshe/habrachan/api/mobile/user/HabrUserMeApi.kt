package com.makentoshe.habrachan.api.mobile.user

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.user.HabrUserApiBuilder
import com.makentoshe.habrachan.api.user.HabrUserMeApi
import com.makentoshe.habrachan.api.user.HabrUserMeApiBuilder

fun HabrUserApiBuilder.me(): HabrUserMeApiBuilder {
    return HabrUserMeApiBuilder(path.plus("/me"))
}

fun HabrUserMeApiBuilder.build(parameters: AdditionalRequestParameters): HabrUserMeApi {
    return HabrUserMeApi(path.plus("/"), parameters.queries, parameters.headers)
}
