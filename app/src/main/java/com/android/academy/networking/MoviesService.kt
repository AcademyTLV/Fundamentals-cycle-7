package com.android.academy.networking

import com.android.academy.networking.NetworkingConstants.MOVIE_ID_KEY
import com.android.academy.networking.NetworkingConstants.POPULAR_QUERY_PATH
import com.android.academy.networking.NetworkingConstants.VIDEOS_QUERY_PATH
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesService {


    @GET(POPULAR_QUERY_PATH)
    fun loadPopularMovies(): Call<MoviesListResult>

    @GET(VIDEOS_QUERY_PATH)
    fun getTrailers(@Path(MOVIE_ID_KEY) movieId: Int): Call<TrailersListResult>


}