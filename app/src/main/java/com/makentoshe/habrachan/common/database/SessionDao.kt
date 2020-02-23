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

    /** Replace the [UserSession] instance by the new one passed from arguments */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userSession: UserSession)

    /** Remove all rows from the database */
    @Query("DELETE FROM usersession")
    fun clear()

    /** Returns all rows in the database. There must be only one [UserSession] instance */
    @Query("SELECT * FROM usersession")
    fun getAll(): List<UserSession>
}