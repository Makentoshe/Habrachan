package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.Geo

@Entity
data class GeoRecord(
    val city: String? = null,
    val country: String? = null,
    val region: String? = null
) {
    constructor(geo: Geo): this(geo.city, geo.country, geo.region)
}