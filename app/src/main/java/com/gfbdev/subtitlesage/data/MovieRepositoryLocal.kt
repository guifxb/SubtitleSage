package com.gfbdev.subtitlesage.data

import com.gfbdev.subtitlesage.model.MovieInfoLocal
import kotlinx.coroutines.flow.Flow

interface MovieRepositoryLocal {

    fun getAllItemsStream(): Flow<List<MovieInfoLocal>>

    fun getItemStream(id: String): Flow<MovieInfoLocal?>

    suspend fun insertItem(item: MovieInfoLocal)

    suspend fun deleteItem(item: MovieInfoLocal)

    suspend fun updateItem(item: MovieInfoLocal)

}

