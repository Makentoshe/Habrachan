package com.makentoshe.habrachan.common.entity.session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserSession(
    @PrimaryKey
    val clientKey: String,
    val apiKey: String,
    val tokenKey: String = "",
    /** Contains request for displaying articles on start */
    val articlesRequestSpec: ArticlesRequestSpec = ArticlesRequestSpec.Interesting()
) {
    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = tokenKey.isNotBlank()

}