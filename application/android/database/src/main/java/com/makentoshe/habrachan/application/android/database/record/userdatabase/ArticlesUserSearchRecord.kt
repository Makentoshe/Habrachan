package com.makentoshe.habrachan.application.android.database.record.userdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This entity contains a single search element that uses in articles screen.
 */
@Entity
data class ArticlesUserSearchRecord(
    /** The title of this search. */
    @PrimaryKey
    val title: String,

    /** Is a default search. Default searches can't be deleted in the application. */
    val isDefault: Boolean,
)
