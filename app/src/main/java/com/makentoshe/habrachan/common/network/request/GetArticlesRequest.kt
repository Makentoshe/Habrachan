package com.makentoshe.habrachan.common.network.request

import android.content.Context
import androidx.room.Entity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.session.UserSession
import java.util.*

data class GetArticlesRequest(
    val client: String,
    val api: String,
    val token: String,
    val page: Int,
    val spec: String,
    val sort: String? = null,
    val include: String? = "text_html",
    val exclude: String? = null
) {

    constructor(
        userSession: UserSession,
        page: Int,
        spec: Spec2
    ) : this(
        userSession.clientKey,
        userSession.apiKey,
        userSession.tokenKey,
        page,
        spec.request,
        spec.sort,
        spec.include,
        spec.exclude
    )

    sealed class Spec2(
        val request: String,
        val sort: String? = null,
        val include: String? = "text_html",
        val exclude: String? = null
    ): java.io.Serializable {

        abstract fun toString(context: Context): String

        @Entity(tableName = "interesting")
        class Interesting: Spec2("posts/interesting") {
            override fun toString(context: Context): String {
                return context.getString(R.string.articles_type_interesting)
            }
        }

        @Entity(tableName = "all")
        class All: Spec2("posts/all") {
            override fun toString(context: Context): String {
                return context.getString(R.string.articles_type_all)
            }
        }

        @Entity(tableName = "subscription")
        class Subscription: Spec2("feed/all") {
            override fun toString(context: Context): String {
                return context.getString(R.string.articles_type_subscription)
            }
        }

        @Entity(tableName = "top")
        class Top(val type: Type): Spec2("top/${type.value}") {

            override fun toString(context: Context): String {
                val prefix = context.getString(R.string.articles_type_top)
                val suffix = type.toString(context).toLowerCase(Locale.getDefault())
                return "$prefix $suffix"
            }

            sealed class Type(val value: String) {

                abstract fun toString(context: Context): String

                object AllTime: Type("alltime") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_alltime)
                    }
                }

                object Yearly: Type("yearly") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_yearly)
                    }
                }

                object Monthly: Type("monthly") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_monthly)
                    }
                }

                object Weekly: Type("weekly") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_weekly)
                    }
                }

                object Daily: Type("daily") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_daily)
                    }
                }
            }
        }

        @Entity(tableName = "search")
        class Search(
            val search: String, val type: Type
        ): Spec2("search/posts/$search", type.value) {

            constructor(search: String, type: String) : this(search, buildType(type))

            override fun toString(context: Context): String {
                val prefix = context.getString(R.string.articles_type_search)
                val suffix = type.toString(context).toLowerCase(Locale.getDefault())
                return "$prefix $suffix"
            }

            sealed class Type(val value: String) {

                abstract fun toString(context: Context): String

                object Date: Type("date") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_search_type_date)
                    }
                }

                object Relevance: Type("relevance") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_search_type_relevance)
                    }
                }

                object Rating: Type("rating") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_search_type_rating)
                    }
                }
            }

            companion object {
                private fun buildType(type: String) = when(type) {
                    "date" -> Type.Date
                    "relevance" -> Type.Relevance
                    "rating" -> Type.Rating
                    else -> throw IllegalArgumentException()
                }
            }
        }
    }
}