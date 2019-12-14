package com.android.academy.networking

import com.google.gson.annotations.SerializedName

data class MoviesListResult(

    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<MovieResult>
)

data class MovieResult(

    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("title") val title: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("original_language") val original_language: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("genre_ids") val genre_ids: List<Int>,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String
)

data class TrailersListResult(

    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<TrailerResult>
)

data class TrailerResult(

    @SerializedName("id") val id: String,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("iso_3166_1") val iso31661: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("size") val size: Int,
    @SerializedName("type") val type: String
)