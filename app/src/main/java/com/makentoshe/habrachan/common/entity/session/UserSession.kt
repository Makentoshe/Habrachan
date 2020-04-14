package com.makentoshe.habrachan.common.entity.session

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val articlesRequest: ArticlesRequest = ArticlesRequest.interesting()
) {
    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = tokenKey.isNotBlank()
}