package com.android.academy.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesService {

    companion object {
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"
        const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

        private const val BASE_URL = "https://api.themoviedb.org"

        /* base search image url */
        const val BASE_API_URL = "$BASE_URL/3/"
        private const val POPULAR = "movie/popular"

        /* api key */
        private const val apiKey = "7e84bac038ebf5780362ef43c48ab6b6"
        private const val keyQuery = "?api_key=$apiKey"

        private const val POPULAR_QUERY_PATH = POPULAR + keyQuery

        private const val MOVIE_ID_KEY = "movie_id"
        private const val VIDEOS = "movie/{$MOVIE_ID_KEY}/videos"

        private const val VIDEOS_QUERY_PATH = VIDEOS + keyQuery
    }

    @GET(POPULAR_QUERY_PATH)
    fun loadPopularMovies(): Call<MoviesListResult>

    @GET(VIDEOS_QUERY_PATH)
    fun getTrailers(@Path(MOVIE_ID_KEY) movieId: Int): Call<TrailersListResult>


}