package com.makentoshe.habrachan.common.database.session

import androidx.room.TypeConverter
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec

class SessionConverters {

    @TypeConverter
    fun specToJson(spec: ArticlesRequestSpec): String {
        return spec.toJson()
    }

    @TypeConverter
    fun jsonToSpec(json: String): ArticlesRequestSpec {
        return ArticlesRequestSpec.fromJson(json)
    }
}