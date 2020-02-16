package com.makentoshe.habrachan.common.entity.session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserSession(
    @PrimaryKey
    val clientKey: String,
    val apiKey: String,
    val tokenKey: String? = null
)