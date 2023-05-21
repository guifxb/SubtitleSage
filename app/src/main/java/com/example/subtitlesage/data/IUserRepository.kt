package com.example.subtitlesage.data

import com.example.subtitlesage.model.Experience
import com.example.subtitlesage.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun getUser(): Flow<UserInfo?>

    suspend fun insertItem(item: UserInfo)

    suspend fun deleteItem(item: UserInfo)

    suspend fun updateItem(item: UserInfo)

}


interface IExpRepository {

    fun getAllItemsStream(): Flow<List<Experience>>

    suspend fun insertItem(item: Experience)

    suspend fun deleteItem(item: Experience)

    suspend fun updateItem(item: Experience)

}