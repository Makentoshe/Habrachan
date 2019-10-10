package com.makentoshe.habrachan.common.model.database

import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class RealmDatabasePostsConfiguration(
    private val configuration: RealmConfiguration
): DatabasePostsConfiguration {

    private val realm: Realm
        get() = Realm.getInstance(configuration)

    override fun get(page: Int): GetRawRequest? {
        realm.use {
            val databaseObject = it.where<PostsRequestRealmDatabaseObject>().findFirst()?: return null
            return try {
                GetRawRequest(databaseObject.path1!!, databaseObject.path2!!, databaseObject.client!!, databaseObject.token, databaseObject.api, page)
            } catch (e: KotlinNullPointerException) {
                null
            }
        }
    }

    override fun put(value: GetRawRequest) {
        realm.use {
            realm.beginTransaction()
            if (get(-1) != null) clear()
            val databaseObject = it.createObject<PostsRequestRealmDatabaseObject>()
            databaseObject.api = value.api
            databaseObject.token = value.token
            databaseObject.client = value.api
            databaseObject.path1 = value.path1
            databaseObject.path2 = value.path2
            realm.commitTransaction()
        }
    }

    override fun clear() {
        realm.deleteAll()
    }
}