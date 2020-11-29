package com.makentoshe.habrachan.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.common.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: User)

    @Query("SELECT * FROM user WHERE LOWER(login) LIKE LOWER(:login)")
    fun getByLogin(login: String): User?

    @Query("DELETE FROM user")
    fun clear()
}