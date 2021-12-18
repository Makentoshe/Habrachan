package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRecord3(
    @PrimaryKey
    val login: String,
    val fullname: String,
    val avatarUrl: String?,
    val speciality: String,
    /** Encoded. @see UserGender for more details*/
    val gender: Int,
    val rating: Int,
    val ratingPosition: Int?,
    val scoresCount: Int,
    val votesCount: Int,
    val lastActivityDateTimeRaw: String,
    val registerDateTimeRaw: String,
    val birthdayRaw: String?,
)