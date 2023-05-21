package com.example.subtitlesage.data

import com.example.subtitlesage.model.MovieInfoNet

interface MovieRepository {
    suspend fun getMovieInfo(): List<MovieInfoNet>
    suspend fun getTitleToAdd(titleSearch: String): MovieInfoNet
}

