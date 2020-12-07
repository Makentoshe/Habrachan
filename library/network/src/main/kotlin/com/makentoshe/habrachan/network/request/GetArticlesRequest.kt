package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession
import java.io.Serializable

/** Request a batch of articles, on selected [page] with selected [spec] */
data class GetArticlesRequest(val session: UserSession, val page: Int, val spec: Spec) {

    sealed class Spec(val request: String) : Serializable {
        abstract val sort: String?
        abstract val include: String?
        abstract val exclude: String?

        class All(
            override val sort: String? = null,
            override val include: String? = null,
            override val exclude: String? = null
        ) : Spec("posts/all")

        class Interesting(
            override val sort: String? = null,
            override val include: String? = null,
            override val exclude: String? = null
        ) : Spec("posts/interesting")

        class Top(
            override val sort: String? = null,
            override val include: String? = null,
            override val exclude: String? = null
        ) : Spec("top/daily")
    }
}
