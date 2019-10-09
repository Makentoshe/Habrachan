package com.makentoshe.habrachan.common.model.database

import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.delete

class RealmDatabasePosts(
    private val configuration: RealmConfiguration
): DatabasePosts {

    private val realm: Realm
        get() = Realm.getInstance(configuration)

    override fun get(key: GetRawRequest): PostsResponse? {
        realm.use {
            val json = it.where(PostsResponseRealmDatabaseObject::class.java).equalTo("page", key.page).findFirst()?.json
            return if (json == null) null else PostsResponse.fromJson(json)
        }
    }

    override fun set(key: GetRawRequest, value: PostsResponse) {
        realm.use {
            it.beginTransaction()
            if (get(key) != null) {
                it.where(PostsResponseRealmDatabaseObject::class.java)
                    .equalTo("page", key.page)
                    .findFirst()
                    ?.deleteFromRealm()
            }
            it.createObject(PostsResponseRealmDatabaseObject::class.java, key.page).apply {
                json = value.toJson()
            }
            it.commitTransaction()
        }
    }

    override fun clear() {
        realm.deleteAll()
    }
}