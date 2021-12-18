package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.cache.record.UserRecord3

@Dao
interface UserDao3 {

    @Query("SELECT * FROM UserRecord3")
    fun getAll(): List<UserRecord3>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: UserRecord3)

    @Query("SELECT * FROM UserRecord3 WHERE login = :login")
    fun getByLogin(login: String): UserRecord3?
}