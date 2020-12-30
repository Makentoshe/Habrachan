package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.GetArticlesRequest

interface UserSession {

    @Deprecated("Seems like it is not required. Last check was in 30.12.2020")
    /** Key for the api access */
    val client: String

    /** Used when user is logged off */
    val api: String

    /** Used when user is logged in. If user is logged off the value should be null */
    val token: String?

    /** Contains request for displaying articles on start */
    val articlesRequestSpec: GetArticlesRequest.Spec

    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = token != null
}

internal fun userSession(client: String, api: String) = object: UserSession {
    override val api = api
    override val client = client
    override val token: String? = null
    override val articlesRequestSpec = GetArticlesRequest.Spec.All()
}
