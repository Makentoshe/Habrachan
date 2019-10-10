package com.makentoshe.habrachan.common.model.database

import io.realm.RealmObject

open class PostsRequestRealmDatabaseObject : RealmObject(), DatabaseObject {
    var client: String? = null
    var token: String? = null
    var path1: String? = null
    var path2: String? = null
    var api: String? = null
}