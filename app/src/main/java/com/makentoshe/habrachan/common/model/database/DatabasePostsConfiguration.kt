package com.makentoshe.habrachan.common.model.database

import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import io.realm.RealmConfiguration

interface DatabasePostsConfiguration: Database {
    
    companion object {
        const val VERSION = 1L
    }

    fun get(page: Int): GetRawRequest?

    fun put(value: GetRawRequest)
    
    class Builder(private val title: String) {
        fun build() : DatabasePostsConfiguration {
            val configuration = RealmConfiguration.Builder().name("$title-config").schemaVersion(VERSION).build()
            return RealmDatabasePostsConfiguration(configuration)
        }
    }
}