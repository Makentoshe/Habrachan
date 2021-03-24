package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.network.UserSession

@Entity
data class UserSessionRecord(
    val client: String,
    val api: String,
    val filterLanguage: String,
    val habrLanguage: String,
    @PrimaryKey
    val token: String
) {
    constructor(userSession: UserSession) : this(
        userSession.client, userSession.api, userSession.filterLanguage, userSession.habrLanguage, userSession.token
    )
}
