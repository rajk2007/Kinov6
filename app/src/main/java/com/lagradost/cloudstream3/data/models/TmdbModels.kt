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
    val overview: String?,
    val popularity: Double?
) {
    val displayTitle: String get() = title ?: name ?: "Unknown"
    val displayDate: String get() = (releaseDate ?: firstAirDate ?: "").take(4)
    val fullPosterPath: String get() = "https://image.tmdb.org/t/p/w342$posterPath"
    val fullBackdropPath: String get() = "https://image.tmdb.org/t/p/original$backdropPath"
}
