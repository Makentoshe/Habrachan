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
    val include: String? = "text_html",
    val exclude: String? = null
) {

    constructor(
        userSession: UserSession, page: Int, spec: Spec, include: String?, exclude: String?
    ) : this(
        userSession.clientKey, userSession.apiKey, userSession.tokenKey, page, spec, include, exclude
    )

    class Spec(val request: String, val sort: String?) {

        init {
            if (!request.startsWith(SEARCH) && !request.startsWith(TOP) && !types.contains(request)) {
                throw IllegalArgumentException(request)
            }
        }

        fun toString(context: Context): String = when (request) {
            INTERESTING -> context.getString(R.string.articles_type_interesting)
            ALL -> context.getString(R.string.articles_type_all)
            SUBSCRIPTION -> context.getString(R.string.articles_type_subscription)
            else -> when {
                request.startsWith(SEARCH) -> {
                    context.getString(R.string.articles_type_search)
                }
                request.startsWith(TOP) -> {
                    context.getString(R.string.articles_type_top)
                }
                else -> toString()
            }
        }

        enum class TopSortTypes(val sort: String) {
            ALLTIME("alltime"), YEARLY("yearly"), DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly")
        }

        enum class SearchSortTypes(val sort: String) {
            DATE("date"), RELEVANCE("relevance"), RATING("rating");
        }

        companion object {
            private const val INTERESTING = "posts/interesting"
            private const val ALL = "posts/all"
            private const val SUBSCRIPTION = "feed/all"
            private const val TOP = "top/"
            private const val SEARCH = "search/posts/"
            private val types = setOf(TOP, INTERESTING, ALL, SUBSCRIPTION, SEARCH)

            fun interesting() = Spec(INTERESTING, null)

            fun all() = Spec(ALL, null)

            fun subscription() = Spec(SUBSCRIPTION, null)

            fun top(sortType: TopSortTypes) = Spec(TOP.plus(sortType.sort), null)

            fun search(search: String, sortTypes: SearchSortTypes) = Spec(SEARCH.plus(search), sortTypes.sort)
        }
    }
}