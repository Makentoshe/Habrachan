package com.makentoshe.habrachan.network

interface UserSession {

    @Deprecated("Seems like it is not required. Last check was in 30.12.2020")
    /** Key for the api access */
    val client: String

    /** Used when user is logged off */
    val api: String

    /** Used when user is logged in. If user is logged off the value should be null */
    val token: String?

    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = token != null

    /**
     * Defines content filter language.
     * Note: useful only in mobile api
     */
    val filterLanguage: String

    /**
     * Defines UI language. When api returns some categories and their descriptions
     * they will be wrote with the selected language.
     * Note: useful only in mobile api
     */
    val habrLanguage: String
}

fun userSession(
    client: String,
    api: String,
    filterLanguage: String = "ru%2Cen",
    habrLanguage: String = "en"
) = object: UserSession {
    override val api = api
    override val client = client
    override val token: String? = null
    override val habrLanguage: String = habrLanguage
    override val filterLanguage: String = filterLanguage
}
