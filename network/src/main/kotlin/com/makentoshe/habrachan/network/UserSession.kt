package com.makentoshe.habrachan.network

interface UserSession {

    @Deprecated("Seems like it is not required. Last check was in 30.12.2020")
    /** Key for the api access */
    val client: String

    /** Used when user is logged off */
    val api: String

    /**
     * Defines content filter language.
     * Note: useful only in mobile api
     */
    var filterLanguage: String

    /**
     * Defines UI language. When api returns some categories and their descriptions
     * they will be wrote with the selected language.
     * Note: useful only in mobile api
     */
    var habrLanguage: String

    /** Used when user is logged in. If user is logged off the value should be empty */
    var token: String

    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = token.isNotEmpty()
}

fun userSession(
    client: String,
    api: String,
    token: String = "",
    filterLanguage: String = "ru%2Cen",
    habrLanguage: String = "en"
) = object: UserSession {
    override val api = api
    override val client = client
    override var token = token
    override var habrLanguage: String = habrLanguage
    override var filterLanguage: String = filterLanguage
}

fun userSession(
    userSession: UserSession,
    client: String = userSession.client,
    api: String = userSession.api,
    token: String = userSession.token,
    filterLanguage: String = userSession.filterLanguage,
    habrLanguage: String = userSession.habrLanguage
) = object: UserSession {
    override val api = api
    override val client = client
    override var token = token
    override var habrLanguage: String = habrLanguage
    override var filterLanguage: String = filterLanguage
}