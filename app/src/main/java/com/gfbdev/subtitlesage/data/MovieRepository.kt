package com.gfbdev.subtitlesage.data

import com.gfbdev.subtitlesage.model.MovieInfoNet

interface MovieRepository {
    suspend fun getMovieInfo(): List<MovieInfoNet>
    suspend fun getTitleToAdd(titleSearch: String): MovieInfoNet
}

