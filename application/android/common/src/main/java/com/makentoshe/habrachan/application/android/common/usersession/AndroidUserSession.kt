package com.makentoshe.habrachan.application.android.common.usersession

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import com.makentoshe.habrachan.functional.toRequire2
import com.makentoshe.habrachan.network.UserSession

@JvmInline
value class ClientId(val string: String) {
    val name: String get() = NAME

    companion object {
        const val NAME = "client"
    }
}

@JvmInline
value class HabrSessionIdCookie(val string: String) {
    val name: String get() = NAME

    companion object {
        const val NAME = "habrsession_id"
    }
}

@JvmInline
value class AccessToken(val string: String) {
    val name: String get() = NAME

    companion object {
        const val NAME = "token"
    }
}

@JvmInline
value class ClientApi(val string: String) {
    val name: String get() = NAME

    companion object {
        const val NAME = "apiKey"
    }
}

data class AndroidUserSession(
    val client: Require2<ClientId>,
    val api: Require2<ClientApi>,
    val accessToken: Option2<AccessToken>,
    val habrSessionId: Option2<HabrSessionIdCookie>,
) {

    constructor(
        client: ClientId,
        api: ClientApi,
        accessToken: AccessToken?,
        habrSessionId: HabrSessionIdCookie?
    ) : this(
        client.toRequire2(),
        api.toRequire2(),
        Option2.from(accessToken),
        Option2.from(habrSessionId),
    )
}

val AndroidUserSession.isUserLoggedIn: Boolean get() = accessToken.isNotEmpty and habrSessionId.isNotEmpty

fun AndroidUserSession.toRequestParameters(): AdditionalRequestParameters {
    val queries = mapOf("fl" to "en,ru")

    val cookies = hashMapOf<String, String>()
    habrSessionId.onNotEmpty { cookies[it.name] = it.string }

    val headers = hashMapOf(
        api.value.name to api.value.string,
        client.value.name to client.value.string,
    )
    accessToken.onNotEmpty { headers[it.name] = it.string }

    return AdditionalRequestParameters(headers, queries, cookies)
}

@Deprecated("Just for backward compatibility", replaceWith = ReplaceWith("AndroidUserSession"))
fun AndroidUserSession.toUserSession() = object : UserSession {
    override val api: String = this@toUserSession.api.value.string
    override val client: String = this@toUserSession.client.value.string

    override var token: String
        get() = this@toUserSession.accessToken.nullableValue?.string ?: ""
        set(value) = Unit

    override var habrLanguage: String
        get() = "en"
        set(value) = Unit

    override var filterLanguage: String
        get() = "ru%2Cen"
        set(value) = Unit
}
