package com.gfbdev.subtitlesage.network

import com.gfbdev.subtitlesage.model.MovieInfoNet
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {
    @GET("?apikey=2ec3f8a4")
    suspend fun getMovieInfo(
        @Query("i") movie : String
    ): MovieInfoNet
}