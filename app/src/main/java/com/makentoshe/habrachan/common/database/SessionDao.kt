package com.makentoshe.habrachan.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.common.entity.session.UserSession

@Dao
interface SessionDao {

    /** Returns first and single row in the database - the current [UserSession] */
    @Query("SELECT * FROM usersession LIMIT 1")
    fun get(): UserSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userSession: UserSession)

    @Query("DELETE FROM usersession")
    fun clear()

    /** Returns first and single row in the database - the current [UserSession] */
    @Query("SELECT * FROM usersession")
    fun getAll(): List<UserSession>
}