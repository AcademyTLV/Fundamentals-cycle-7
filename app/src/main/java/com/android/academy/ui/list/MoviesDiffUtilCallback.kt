package com.android.academy.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.android.academy.model.MovieModel

class MoviesDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {

    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }
}