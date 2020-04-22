package com.makentoshe.habrachan.common.database.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.common.entity.User

@Dao
abstract class MeDao {

    /** Returns first and single row in the database - the current logged in [User] */
    @Query("SELECT * FROM user LIMIT 1")
    abstract fun get(): User

    /** Replace the [User] instance by the new one passed from arguments */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: User)

    /** Remove all rows from the database */
    @Query("DELETE FROM user")
    abstract fun clear()

    val isEmpty: Boolean
        get() = get() == null
}