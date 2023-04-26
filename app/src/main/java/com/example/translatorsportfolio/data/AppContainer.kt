package com.example.translatorsportfolio.data

import android.content.Context
import com.example.translatorsportfolio.network.AppApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val movieRepository: MovieRepository
    val movieRepositoryLocal: MovieRepositoryLocal
    val userRepository: UserRepository
    val expRepository: ExpRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val BASE_URL = "https://www.omdbapi.com"
    private val json = Json { ignoreUnknownKeys = true }

    override val movieRepository: MovieRepository by lazy {
        NetworkMovieRepository(retrofitService)
    }

    override val movieRepositoryLocal: MovieRepositoryLocal by lazy {
        MovieRepositoryOffline(MovieDatabase.getDatabase(context).movieDao())
    }

    override val userRepository: UserRepository by lazy {
        UserRepository(UserDatabase.getDatabase(context).userDao())
    }

    override val expRepository: ExpRepository by lazy {
        ExpRepository(UserDatabase.getDatabase(context).expDao())
    }

    //Retrofit builder
    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: AppApiService by lazy {
        retrofit.create(AppApiService::class.java)
    }
}