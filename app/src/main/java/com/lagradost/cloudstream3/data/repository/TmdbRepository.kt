package com.lagradost.cloudstream3.data.repository

import com.lagradost.cloudstream3.data.api.TmdbApi
import com.lagradost.cloudstream3.data.models.MediaItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TmdbRepository {
    private val apiKey = "cf5a2b948bb3cbe03332dc70594b4ba7"
    
    private val api = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbApi::class.java)

    suspend fun getTrending(): List<MediaItem> = api.getTrending(apiKey = apiKey).results
    suspend fun getPopularMovies(): List<MediaItem> = api.getPopularMovies(apiKey = apiKey).results
    suspend fun getPopularTv(): List<MediaItem> = api.getPopularTv(apiKey = apiKey).results
    suspend fun getTopRatedMovies(): List<MediaItem> = api.getTopRatedMovies(apiKey = apiKey).results
    suspend fun getAnime(): List<MediaItem> = api.getAnime(apiKey = apiKey).results
}
