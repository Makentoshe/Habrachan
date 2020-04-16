package com.makentoshe.habrachan.common.network.request

import android.content.Context
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.session.UserSession

data class GetArticlesRequest(
    val client: String,
    val api: String,
    val token: String,
    val page: Int,
    val spec: Spec,
    val include: List<String> = listOf("text_html"),
    val exclude: List<String> = listOf()
) {

    constructor(
        userSession: UserSession, page: Int, spec: Spec, include: List<String>, exclude: List<String>
    ) : this(
        userSession.clientKey, userSession.apiKey, userSession.tokenKey, page, spec, include, exclude
    )

    val includeString = include.joinToString(", ")
    val excludeString = exclude.joinToString(", ")

    class Spec(val request: String, val sort: String?) {

        init {
            if (!types.contains(request)) throw IllegalArgumentException(request)
        }

        fun toString(context: Context): String = when (request) {
            INTERESTING -> context.getString(R.string.articles_type_interesting)
            ALL -> context.getString(R.string.articles_type_all)
            SUBSCRIPTION -> context.getString(R.string.articles_type_subscription)
            TOP_ALLTIME -> context.getString(R.string.articles_type_top_alltime)
            else -> throw IllegalStateException()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Spec
            return request == other.request
        }

        override fun hashCode() = request.hashCode()


        companion object {
            private const val INTERESTING = "posts/interesting"
            private const val ALL = "posts/all"
            private const val SUBSCRIPTION = "feed/all"
            private const val TOP_ALLTIME = "top/alltime"
            private const val TOP_DAILY = "top/daily"
            private const val TOP_WEEKLY = "top/weekly"
            private const val TOP_MONTHLY = "top/monthly"
            private const val TOP_YEARLY = "top/yearly"
            private const val SEARCH = "search/posts/Android"
            private val types = setOf(
                TOP_ALLTIME, TOP_YEARLY, TOP_MONTHLY, TOP_WEEKLY, TOP_DAILY,
                INTERESTING,
                ALL,
                SUBSCRIPTION,
                SEARCH
            )

            fun interesting() = Spec(INTERESTING, null)

            fun all() = Spec(ALL, null)

            fun subscription() = Spec(SUBSCRIPTION, null)

            fun topAlltime() = Spec(TOP_ALLTIME, null)

            fun search(sort: String) = Spec(SEARCH, sort)
        }
    }
}