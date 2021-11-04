package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.network.UserSession

@Entity
data class UserSessionRecord(
    val client: String,
    val api: String,
    val filterLanguage: String,
    val habrLanguage: String,
    @PrimaryKey
    val token: String,
    @Embedded
    val userRecord: UserRecord?
) {
    constructor(userSession: UserSession, user: User?) : this(
        userSession.client,
        userSession.api,
        userSession.filterLanguage,
        userSession.habrLanguage,
        userSession.token,
        user?.let(::UserRecord)
    )

    constructor(
        client: String, api: String, filterLanguage: String, habrLanguage: String, token: String, user: User?
    ) : this(client, api, filterLanguage, habrLanguage, token, user?.let(::UserRecord))
}
