package com.gfbdev.subtitlesage.data

import com.gfbdev.subtitlesage.model.MovieInfoNet
import com.gfbdev.subtitlesage.network.AppApiService


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