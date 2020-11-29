package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.GetArticlesRequest

interface UserSession {
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

