package com.gfbdev.subtitlesage.data

import androidx.room.*
import com.gfbdev.subtitlesage.model.MovieInfoLocal
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieInfoLocal: MovieInfoLocal)

    @Update
    suspend fun update(movieInfoLocal: MovieInfoLocal)

    @Delete
    suspend fun delete(movieInfoLocal: MovieInfoLocal)

    @Query("SELECT * from movies WHERE imdbid = :id")
    fun getItem(id: String): Flow<MovieInfoLocal>

    @Query("SELECT * from movies ORDER BY title ASC")
    fun getAllItems(): Flow<List<MovieInfoLocal>>

}