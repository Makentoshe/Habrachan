package com.makentoshe.habrachan.application.android.database

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.AvatarRecord

@Dao
interface AvatarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: AvatarRecord)

    @Query("SELECT * FROM AvatarRecord")
    fun getAll(): List<AvatarRecord>

    @Query("SELECT COUNT(*) FROM AvatarRecord")
    fun count(): Long

    @Query("SELECT * FROM AvatarRecord WHERE history IN (SELECT history FROM AvatarRecord ORDER BY history ASC LIMIT :n)")
    fun last(n: Int): List<AvatarRecord>

    @Query("DELETE FROM AvatarRecord WHERE history IN (SELECT history FROM AvatarRecord ORDER BY history ASC LIMIT :n)")
    fun dropLast(n: Int)

    @Delete
    fun delete(record: AvatarRecord)

    @Query("DELETE FROM AvatarRecord")
    fun clear()
}