package com.gfbdev.subtitlesage.data

import com.gfbdev.subtitlesage.model.MovieInfoLocal
import kotlinx.coroutines.flow.Flow

class MovieRepositoryOffline(private val movieDao: MovieDao) : MovieRepositoryLocal {

    override fun getAllItemsStream(): Flow<List<MovieInfoLocal>> = movieDao.getAllItems()

    override fun getItemStream(id: String): Flow<MovieInfoLocal?> = movieDao.getItem(id)

    override suspend fun insertItem(item: MovieInfoLocal) = movieDao.insert(item)

    override suspend fun deleteItem(item: MovieInfoLocal) = movieDao.delete(item)

    override suspend fun updateItem(item: MovieInfoLocal) = movieDao.update(item)
}