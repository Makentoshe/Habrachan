package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.Metadata

@Entity
data class MetadataRecord(
    val description: String? = null, val gaPageType: String? = null, val metaImage: String? = null
) {
    constructor(metadata: Metadata) : this(
        metadata.description, metadata.gaPageType, metadata.metaImage
    )

    fun toMetadata() = Metadata(description, gaPageType, metaImage)
}