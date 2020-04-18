package com.makentoshe.habrachan.common.network.request

import android.content.Context
import androidx.room.Entity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
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
        spec: Spec
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

    @Entity
    sealed class Spec(
        @SerializedName("request")
        val request: String,
        @SerializedName("sort")
        val sort: String? = null,
        @SerializedName("include")
        val include: String? = "text_html",
        @SerializedName("exclude")
        val exclude: String? = null
    ) : java.io.Serializable {

        abstract fun toString(context: Context): String

        fun toJson(): String {
            return Gson().toJson(this)
        }

        class Interesting : Spec("posts/interesting") {
            override fun toString(context: Context): String {
                return context.getString(R.string.articles_type_interesting)
            }
        }

        class All : Spec("posts/all") {
            override fun toString(context: Context): String {
                return context.getString(R.string.articles_type_all)
            }
        }

        class Subscription : Spec("feed/all") {
            override fun toString(context: Context): String {
                return context.getString(R.string.articles_type_subscription)
            }
        }

        class Top(val type: Type) : Spec("top/${type.value}") {

            constructor(type: String) : this(buildType(type))

            override fun toString(context: Context): String {
                val prefix = context.getString(R.string.articles_type_top)
                val preposition = context.getString(R.string.articles_top_preposition)
                val suffix = type.toString(context).toLowerCase(Locale.getDefault())
                return "$prefix $preposition $suffix"
            }

            sealed class Type(val value: String): java.io.Serializable {

                abstract fun toString(context: Context): String

                object AllTime : Type("alltime") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_alltime)
                    }
                }

                object Yearly : Type("yearly") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_yearly)
                    }
                }

                object Monthly : Type("monthly") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_monthly)
                    }
                }

                object Weekly : Type("weekly") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_weekly)
                    }
                }

                object Daily : Type("daily") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_top_type_daily)
                    }
                }
            }

            companion object {
                fun buildType(type: String) = when (type) {
                    "alltime" -> Type.AllTime
                    "yearly" -> Type.Yearly
                    "monthly" -> Type.Monthly
                    "weekly" -> Type.Weekly
                    "daily" -> Type.Daily
                    else -> throw IllegalArgumentException()
                }
            }
        }

        class Search(
            val search: String, val type: Type
        ) : Spec("search/posts/$search", type.value) {

            constructor(search: String, type: String) : this(search, buildType(type))

            override fun toString(context: Context): String {
                val prefix = context.getString(R.string.articles_type_search)
                val preposition = context.getString(R.string.articles_search_preposition)
                val suffix = type.toString(context).toLowerCase(Locale.getDefault())
                return "$prefix $preposition $suffix"
            }

            sealed class Type(val value: String) : java.io.Serializable {

                abstract fun toString(context: Context): String

                object Date : Type("date") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_search_type_date)
                    }
                }

                object Relevance : Type("relevance") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_search_type_relevance)
                    }
                }

                object Rating : Type("rating") {
                    override fun toString(context: Context): String {
                        return context.getString(R.string.articles_search_type_rating)
                    }
                }
            }

            companion object {
                private fun buildType(type: String) = when (type) {
                    "date" -> Type.Date
                    "relevance" -> Type.Relevance
                    "rating" -> Type.Rating
                    else -> throw IllegalArgumentException()
                }
            }
        }

        companion object {
            fun fromJson(string: String): Spec {
                val map = Gson().fromJson(string, HashMap::class.java)
                return when (val request = map["request"].toString()) {
                    "posts/interesting" -> Interesting()
                    "posts/all" -> All()
                    "feed/all" -> Subscription()
                    else -> when {
                        request.startsWith("top/") -> {
                            Top(request.removePrefix("top/"))
                        }
                        request.startsWith("search/posts/") -> {
                            Search(request.removePrefix("search/posts/"), map["sort"].toString())
                        }
                        else -> throw IllegalArgumentException()
                    }
                }
            }
        }
    }
}