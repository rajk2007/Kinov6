#!/bin/bash

# Update TmdbApi.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/data/api/TmdbApi.kt
package com.lagradost.cloudstream3.data.api

import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.data.models.TmdbResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrending(
        @Path("media_type") mediaType: String = "all",
        @Path("time_window") timeWindow: String = "day",
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("discover/movie")
    suspend fun getIndianMovies(
        @Query("api_key") apiKey: String,
        @Query("with_original_language") language: String = "hi",
        @Query("region") region: String = "IN"
    ): TmdbResponse

    @GET("discover/tv")
    suspend fun getAnime(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genres: String = "16"
    ): TmdbResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MediaItem

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): TmdbResponse

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): TmdbResponse
}
EOF

# Update TmdbRepository.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/data/repository/TmdbRepository.kt
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
    suspend fun getUpcomingMovies(): List<MediaItem> = api.getUpcomingMovies(apiKey = apiKey).results
    suspend fun getIndianMovies(): List<MediaItem> = api.getIndianMovies(apiKey = apiKey).results
    suspend fun getAnime(): List<MediaItem> = api.getAnime(apiKey = apiKey).results
    
    suspend fun getMovieDetails(movieId: Int): MediaItem = api.getMovieDetails(movieId, apiKey)
    suspend fun getSimilarMovies(movieId: Int): List<MediaItem> = api.getSimilarMovies(movieId, apiKey).results
    suspend fun search(query: String): List<MediaItem> = api.searchMulti(apiKey, query).results
}
EOF
