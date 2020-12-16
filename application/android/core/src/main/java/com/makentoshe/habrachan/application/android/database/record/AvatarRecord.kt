package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Special record entity for indexing avatars cached in file storage. */
@Entity
data class AvatarRecord(
    /** Relative cached file location */
    val location: String,
    /** Just a file url */
    val url: String
) {

    /** Contains record position and used in cache directly */
    @PrimaryKey(autoGenerate = true)
    var history: Long = 0L
}