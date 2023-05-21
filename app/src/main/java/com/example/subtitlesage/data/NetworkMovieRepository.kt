package com.example.subtitlesage.data

import com.example.subtitlesage.model.MovieInfoNet
import com.example.subtitlesage.network.AppApiService


class NetworkMovieRepository(
    private val appApiService: AppApiService,
) : MovieRepository {
    override suspend fun getMovieInfo(): List<MovieInfoNet> {
        val listToReturn: MutableList<MovieInfoNet> = mutableListOf()
        for (movie in PortfolioData.movies) listToReturn.add(appApiService.getMovieInfo(movie))
        return listToReturn
    }

    override suspend fun getTitleToAdd(titleSearch: String): MovieInfoNet {
        return appApiService.getMovieInfo(titleSearch)
    }
}