package com.makentoshe.habrachan.common.model.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PostsResponseRealmDatabaseObject(
    @PrimaryKey
    var page: Int? = null,
    var json: String? = null
): RealmObject(), DatabaseObject