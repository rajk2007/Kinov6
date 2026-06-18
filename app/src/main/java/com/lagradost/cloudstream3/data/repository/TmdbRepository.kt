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
    suspend fun getNowPlaying(): List<MediaItem> = api.getNowPlaying(apiKey = apiKey).results
    suspend fun getHollywood(): List<MediaItem> = api.discoverMovies(apiKey, language = "en").results
    suspend fun getBollywood(): List<MediaItem> = api.discoverMovies(apiKey, language = "hi").results
    suspend fun getKoreanDrama(): List<MediaItem> = api.discoverTv(apiKey, language = "ko").results
    suspend fun getJapanese(): List<MediaItem> = api.discoverMovies(apiKey, language = "ja").results
    suspend fun getAnime(): List<MediaItem> = api.discoverTv(apiKey, genres = "16").results
    suspend fun getPopularTv(): List<MediaItem> = api.getPopularTv(apiKey = apiKey).results
    suspend fun getTopRatedMovies(): List<MediaItem> = api.getTopRatedMovies(apiKey = apiKey).results
    suspend fun getAction(): List<MediaItem> = api.discoverMovies(apiKey, genres = "28").results
    suspend fun getComedy(): List<MediaItem> = api.discoverMovies(apiKey, genres = "35").results
    suspend fun getHorror(): List<MediaItem> = api.discoverMovies(apiKey, genres = "27").results
    suspend fun getSciFi(): List<MediaItem> = api.discoverMovies(apiKey, genres = "878").results
    
    suspend fun getMovieDetails(movieId: Int): MediaItem = api.getMovieDetails(movieId, apiKey)
    suspend fun getSimilarMovies(movieId: Int): List<MediaItem> = api.getSimilarMovies(movieId, apiKey).results
    suspend fun search(query: String): List<MediaItem> = api.searchMulti(apiKey, query).results
}
