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
value class ConnectSidCookie(val string: String) {
    val name: String get() = NAME

    companion object {
        const val NAME = "connect_sid"
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
    val connectSid: Option2<ConnectSidCookie>,
) {

    constructor(
        client: ClientId,
        api: ClientApi,
        accessToken: AccessToken?,
        habrSessionId: HabrSessionIdCookie?,
        connectSidCookie: ConnectSidCookie?,
    ) : this(
        client.toRequire2(),
        api.toRequire2(),
        Option2.from(accessToken),
        Option2.from(habrSessionId),
        Option2.from(connectSidCookie),
    )
}

val AndroidUserSession.isUserLoggedIn: Boolean get() = accessToken.isNotEmpty and habrSessionId.isNotEmpty

fun AndroidUserSession.toRequestParameters(): AdditionalRequestParameters {
    val queries = mapOf("fl" to "en,ru")

    val cookies = hashMapOf<String, String>()
    habrSessionId.onNotEmpty { cookies[it.name] = it.string }
    connectSid.onNotEmpty { cookies[it.name] = it.string }

    val headers = hashMapOf(
        api.value.name to api.value.string,
        client.value.name to client.value.string,
    )
    accessToken.onNotEmpty { headers[it.name] = it.string }

    return AdditionalRequestParameters(headers, queries, cookies)
}

@Deprecated("UserSession interface should be replaced")
fun AndroidUserSession.toUserSession() = object : UserSession {
    override val client: String = this@toUserSession.client.value.string
    override val api: String = this@toUserSession.api.value.string
    override var token: String = this@toUserSession.accessToken.getOrNull()?.string ?: ""
    override var filterLanguage = "en,ru"
    override var habrLanguage = "en"
}