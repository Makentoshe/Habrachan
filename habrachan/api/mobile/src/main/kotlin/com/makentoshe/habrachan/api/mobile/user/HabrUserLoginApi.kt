package com.makentoshe.habrachan.api.mobile.user

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.user.HabrUserApiBuilder
import com.makentoshe.habrachan.api.user.HabrUserLoginApiBuilder
import com.makentoshe.habrachan.api.user.HabrUserLoginCardApi
import com.makentoshe.habrachan.api.user.HabrUserLoginCardApiBuilder
import com.makentoshe.habrachan.entity.user.component.UserLogin

fun HabrUserApiBuilder.login(login: UserLogin): HabrUserLoginApiBuilder {
    return HabrUserLoginApiBuilder(path.plus("/users/${login.string}"))
}

fun HabrUserLoginApiBuilder.card(): HabrUserLoginCardApiBuilder {
    return HabrUserLoginCardApiBuilder(path.plus("/card"))
}

fun HabrUserLoginCardApiBuilder.build(parameters: AdditionalRequestParameters): HabrUserLoginCardApi {
    return HabrUserLoginCardApi(path, parameters.queries, parameters.headers)
}