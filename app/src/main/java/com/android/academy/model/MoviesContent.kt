package com.android.academy.model

import java.util.*

object MoviesContent {

    val movies = ArrayList<MovieModel>()

    fun clear() {
        movies.clear()
    }

    fun addMovie(movie: MovieModel) {
        movies.add(movie)
    }
}
