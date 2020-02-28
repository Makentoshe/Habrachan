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
    @Embedded(prefix = "user_")
    val me: User? = null
) {
    /** Returns true if user already logged in */
    val isLoggedIn: Boolean
        get() = tokenKey.isNotBlank()
}