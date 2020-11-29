package com.makentoshe.habrachan.common.database.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.common.entity.session.UserSession

@Dao
abstract class SessionDao {

    /** Returns first and single row in the database - the current [UserSession] */
    @Query("SELECT * FROM usersession LIMIT 1")
    abstract fun get(): UserSession

    /** Replace the [UserSession] instance by the new one passed from arguments */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(userSession: UserSession)

    val isEmpty: Boolean
        get() = get() == null

}
