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
    suspend fun getAnime(): List<MediaItem> = api.discoverTv(apiKey, genres = "16", language = "ja").results
    suspend fun getPopularTv(): List<MediaItem> = api.getPopularTv(apiKey = apiKey).results
    suspend fun getTopRatedMovies(): List<MediaItem> = api.getTopRatedMovies(apiKey = apiKey).results
    suspend fun getUpcomingMovies(): List<MediaItem> = api.getUpcomingMovies(apiKey = apiKey).results
    suspend fun getTopRatedTv(): List<MediaItem> = api.getTopRatedTv(apiKey = apiKey).results
    suspend fun getAiringTodayTv(): List<MediaItem> = api.getAiringTodayTv(apiKey = apiKey).results
    suspend fun getActionMovies(): List<MediaItem> = api.discoverMovies(apiKey, genres = "28").results
    suspend fun getComedyMovies(): List<MediaItem> = api.discoverMovies(apiKey, genres = "35").results
    suspend fun getHorrorMovies(): List<MediaItem> = api.discoverMovies(apiKey, genres = "27").results
    suspend fun getSciFiMovies(): List<MediaItem> = api.discoverMovies(apiKey, genres = "878").results
    suspend fun getCrimeTv(): List<MediaItem> = api.discoverTv(apiKey, genres = "80").results
    suspend fun getDramaTv(): List<MediaItem> = api.discoverTv(apiKey, genres = "18").results
    suspend fun getSciFiTv(): List<MediaItem> = api.discoverTv(apiKey, genres = "10765").results
    suspend fun getTopRatedAnime(): List<MediaItem> = api.discoverTv(apiKey, genres = "16", language = "ja", sortBy = "vote_average.desc").results
    suspend fun getActionAnime(): List<MediaItem> = api.discoverTv(apiKey, genres = "16,28", language = "ja").results
    suspend fun getRomanceAnime(): List<MediaItem> = api.discoverTv(apiKey, genres = "16,10749", language = "ja").results
    suspend fun getFantasyAnime(): List<MediaItem> = api.discoverTv(apiKey, genres = "16,14", language = "ja").results
    
    suspend fun getMovieDetails(movieId: Int): MediaItem = api.getMovieDetails(movieId, apiKey)
    suspend fun getSimilarMovies(movieId: Int): List<MediaItem> = api.getSimilarMovies(movieId, apiKey).results
    suspend fun search(query: String): List<MediaItem> = api.searchMulti(apiKey, query).results
}
