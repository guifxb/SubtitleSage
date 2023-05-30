package com.gfbdev.subtitlesage.data

import com.gfbdev.subtitlesage.model.Experience
import com.gfbdev.subtitlesage.model.UserInfo
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao): IUserRepository {

    override fun getUser(): Flow<UserInfo?> = userDao.getUser()

    override suspend fun insertItem(item: UserInfo) = userDao.insert(item)

    override suspend fun deleteItem(item: UserInfo) = userDao.delete(item)

    override suspend fun updateItem(item: UserInfo) = userDao.update(item)
}


class ExpRepository(private val expDao: ExperienceDao): IExpRepository {

    override fun getAllItemsStream(): Flow<List<Experience>> = expDao.getExp()

    override suspend fun insertItem(item: Experience) = expDao.insert(item)

    override suspend fun deleteItem(item: Experience) = expDao.delete(item)

    override suspend fun updateItem(item: Experience) = expDao.update(item)
}



