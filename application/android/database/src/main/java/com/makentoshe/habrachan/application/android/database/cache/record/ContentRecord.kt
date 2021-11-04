package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO same as AvatarRecord
@Entity
data class ContentRecord(val url: String, val path: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}