package com.makentoshe.habrachan.common.entity.session

import android.content.Context
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.User

@Entity
data class UserSession(
    @PrimaryKey
    val clientKey: String,
    val apiKey: String,
    val tokenKey: String = "",
    /** User instance */
    @Embedded(prefix = "user_")
    val me: User? = null,
    /** Initial request for displaying articles */
    @Embedded(prefix = "articles_request_")
    val articlesRequestSpec: ArticlesRequestSpec = ArticlesRequestSpec.interesting()
) {
    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = tokenKey.isNotBlank()


    @Entity
    data class ArticlesRequestSpec(val request: String) {

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

        companion object {
            private const val INTERESTING = "posts/interesting"
            private const val ALL = "posts/all"
            private const val SUBSCRIPTION = "feed/all"
            private const val TOP_ALLTIME = "top/alltime"
            private val types = setOf(INTERESTING, ALL, SUBSCRIPTION, TOP_ALLTIME)

            fun interesting() = ArticlesRequestSpec(INTERESTING)

            fun all() = ArticlesRequestSpec(ALL)

            fun subscription() = ArticlesRequestSpec(SUBSCRIPTION)

            fun topAlltime() = ArticlesRequestSpec(TOP_ALLTIME)

            fun fromString(context: Context, string: String): ArticlesRequestSpec {
                if (string == context.getString(R.string.articles_type_interesting)) {
                    return interesting()
                }
                if (string == context.getString(R.string.articles_type_all)) {
                    return all()
                }
                if (string == context.getString(R.string.articles_type_subscription)) {
                    return subscription()
                }
                if (string == context.getString(R.string.articles_type_top_alltime)) {
                    return topAlltime()
                }
                throw IllegalArgumentException(string)
            }
        }
    }
}