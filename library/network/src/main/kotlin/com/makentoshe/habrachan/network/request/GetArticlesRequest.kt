package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession
import java.io.Serializable

/** Request a batch of articles, on selected [page] with selected [spec] */
data class GetArticlesRequest(val session: UserSession, val page: Int, val spec: Spec) : Serializable {

    val count = 20

    sealed class Spec(val request: String) : Serializable {
        abstract val sort: String?
        abstract val include: String?
        abstract val exclude: String?

        data class All(
            override val sort: String? = null,
            override val include: String? = null,
            override val exclude: String? = null
        ) : Spec("posts/all")

        data class Interesting(
            override val sort: String? = null,
            override val include: String? = null,
            override val exclude: String? = null
        ) : Spec("posts/interesting")

        data class Top(
            val type: Type,
            override val sort: String? = null,
            override val include: String? = null,
            override val exclude: String? = null
        ) : Spec("top/${type.value}"), Serializable {

            sealed class Type(val value: String) : Serializable {

                object AllTime : Type("alltime")

                object Yearly : Type("yearly")

                object Monthly : Type("monthly")

                object Weekly : Type("weekly")

                object Daily : Type("daily")
            }
        }

        companion object {
            fun build(request: String?): Spec? = when (request) {
                All().request -> All()
                Interesting().request -> Interesting()
                Top(Top.Type.AllTime).request -> Top(Top.Type.AllTime)
                Top(Top.Type.Yearly).request -> Top(Top.Type.Yearly)
                Top(Top.Type.Monthly).request -> Top(Top.Type.Monthly)
                Top(Top.Type.Weekly).request -> Top(Top.Type.Weekly)
                Top(Top.Type.Daily).request -> Top(Top.Type.Daily)
                else -> null
            }
        }
    }
}
