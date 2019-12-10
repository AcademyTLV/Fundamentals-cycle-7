package com.android.academy.ui.list

import com.android.academy.model.MovieModel

interface OnMovieClickListener {
    fun onMovieClicked(movie: MovieModel, adapterPosition: Int)
}