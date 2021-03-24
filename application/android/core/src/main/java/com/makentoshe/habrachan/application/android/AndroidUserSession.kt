package com.makentoshe.habrachan.application.android

import com.makentoshe.habrachan.application.android.database.dao.UserSessionDao
import com.makentoshe.habrachan.application.android.database.record.UserSessionRecord
import com.makentoshe.habrachan.network.UserSession

data class AndroidUserSession(private val dao: UserSessionDao) : UserSession {

    override val api: String = dao.get().api

    override val client: String = dao.get().client

    override var token: String = dao.get().token
        set(value) {
            field = value
            dao.clearAndInsert(UserSessionRecord(client, api, filterLanguage, habrLanguage, value))
        }

    override var filterLanguage: String = dao.get().filterLanguage
        set(value) {
            dao.clearAndInsert(UserSessionRecord(client, api, value, habrLanguage, token))
            field = value
        }

    override var habrLanguage: String = dao.get().habrLanguage
        set(value) {
            dao.clearAndInsert(UserSessionRecord(client, api, filterLanguage, value, token))
            field = value
        }
}