#!/bin/bash
mkdir -p app/src/main/java/com/lagradost/cloudstream3/data/models
mkdir -p app/src/main/java/com/lagradost/cloudstream3/data/api
mkdir -p app/src/main/java/com/lagradost/cloudstream3/data/repository

# Models
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/data/models/TmdbModels.kt
package com.lagradost.cloudstream3.data.models

import com.google.gson.annotations.SerializedName

data class TmdbResponse(
    val results: List<MediaItem>
)

data class MediaItem(
    val id: Int,
    val title: String?,
    val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    val overview: String?
) {
    val displayTitle: String get() = title ?: name ?: "Unknown"
    val displayDate: String get() = (releaseDate ?: firstAirDate ?: "").take(4)
    val fullPosterPath: String get() = "https://image.tmdb.org/t/p/w342\$posterPath"
    val fullBackdropPath: String get() = "https://image.tmdb.org/t/p/original\$backdropPath"
}
EOF

# API Interface
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/data/api/TmdbApi.kt
package com.lagradost.cloudstream3.data.api

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

    @GET("discover/tv")
    suspend fun getAnime(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genres: String = "16"
    ): TmdbResponse
}
EOF

# Repository
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
    suspend fun getAnime(): List<MediaItem> = api.getAnime(apiKey = apiKey).results
}
EOF
