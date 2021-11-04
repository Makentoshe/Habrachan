package com.makentoshe.habrachan.application.android.common

import com.makentoshe.habrachan.application.android.database.cache.dao.UserSessionDao
import com.makentoshe.habrachan.application.android.database.cache.record.UserSessionRecord
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.network.UserSession

data class AndroidUserSession2(
    private val dao: UserSessionDao
) : UserSession {

    override val api: String = dao.get().api

    override val client: String = dao.get().client

    override var token: String = dao.get().token
        set(value) {
            dao.clearAndInsert(UserSessionRecord(client, api, filterLanguage, habrLanguage, value, user))
            field = value
        }

    override var filterLanguage: String = dao.get().filterLanguage
        set(value) {
            dao.clearAndInsert(UserSessionRecord(client, api, value, habrLanguage, token, user))
            field = value
        }

    override var habrLanguage: String = dao.get().habrLanguage
        set(value) {
            dao.clearAndInsert(UserSessionRecord(client, api, filterLanguage, value, token, user))
            field = value
        }

    var user: User? = dao.get().userRecord?.toUser()
        set(value) {
            dao.clearAndInsert(UserSessionRecord(client, api, filterLanguage, habrLanguage, token, value))
            field = value
        }

    var cookies: List<String> = emptyList()
}
