package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.UserSession

data class GetArticlesRequest(
    val client: String,
    val api: String,
    val token: String,
    val page: Int,
    val spec: String
) {

    class Factory(private val session: UserSession) {

        fun all(page: Int): GetArticlesRequest {
            return GetArticlesRequest(session.clientKey, session.apiKey, session.tokenKey, page, "posts/all")
        }

        fun interesting(page: Int): GetArticlesRequest {
            return GetArticlesRequest(session.clientKey, session.apiKey, session.tokenKey, page, "posts/interesting")
        }

        fun custom(page: Int, spec: String): GetArticlesRequest {
            return GetArticlesRequest(session.clientKey, session.apiKey, session.tokenKey, page, spec)
        }

        fun last(page: Int): GetArticlesRequest {
            return GetArticlesRequest(session.clientKey, session.apiKey, session.tokenKey, page, session.articlesRequest)
        }
    }
}