package com.android.academy.repos

import com.android.academy.networking.RestClient

class MoviesRepository {
    fun getMovies() = RestClient.moviesService.getPopularMovies()
}