package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession
import java.io.Serializable

/** Request a batch of articles, on selected [page] with selected [spec] */
data class GetArticlesRequest(val session: UserSession, val page: Int, val spec: Spec) {

    interface Spec : Serializable {
        val request: String
        val sort: String?
        val include: String?
        val exclude: String?
        override fun toString(): String
    }
}
