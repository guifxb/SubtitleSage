package com.example.translatorsportfolio.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.translatorsportfolio.model.Experience
import com.example.translatorsportfolio.model.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfo: UserInfo)

    @Update
    suspend fun update(userInfo: UserInfo)

    @Delete
    suspend fun delete(userInfo: UserInfo)

    @Query("SELECT * from user")
    fun getUser(): Flow<UserInfo?>

}

@Dao
interface ExperienceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience)

    @Update
    suspend fun update(experience: Experience)

    @Delete
    suspend fun delete(experience: Experience)

    @Query("SELECT * from user_experience ORDER BY company DESC")
    fun getExp(): Flow<List<Experience>>

}