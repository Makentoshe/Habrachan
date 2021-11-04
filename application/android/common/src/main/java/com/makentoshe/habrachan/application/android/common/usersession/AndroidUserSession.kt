package com.makentoshe.habrachan.application.android.common.usersession

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import com.makentoshe.habrachan.functional.toRequire2

@JvmInline
value class ClientId(val string: String)

@JvmInline
value class HabrSessionIdCookie(val string: String)

@JvmInline
value class Token(val string: String)

@JvmInline
value class ClientApi(val string: String)

data class AndroidUserSession(
    val client: Require2<ClientId>,
    val api: Require2<ClientApi>,
    val token: Option2<Token>,
    val habrSessionId: Option2<HabrSessionIdCookie>,
) {

    constructor(
        client: ClientId,
        api: ClientApi,
        token: Token?,
        habrSessionId: HabrSessionIdCookie?
    ) : this(
        client.toRequire2(),
        api.toRequire2(),
        Option2.from(token),
        Option2.from(habrSessionId),
    )
}

fun AndroidUserSession.toRequestParameters(): AdditionalRequestParameters {
    val queries = mapOf("fl" to "en%2Cru")

    val headers = hashMapOf(
        "apiKey" to api.value.string,
        "client" to client.value.string,
    )
    token.onNotEmpty { headers["token"] = it.string }

    return AdditionalRequestParameters(headers, queries)
}
