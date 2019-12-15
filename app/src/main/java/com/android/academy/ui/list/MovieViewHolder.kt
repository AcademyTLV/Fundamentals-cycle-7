package com.android.academy.ui.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieViewHolder(view: View, onMovieClickListener: OnMovieClickListener) : RecyclerView.ViewHolder(view) {

    private val ivImage: ImageView = view.item_movie_iv
    private val tvTitle: TextView = view.item_movie_tv_title
    private val tvOverview: TextView = view.item_movie_tv_overview

    init {
        view.setOnClickListener {
            onMovieClickListener.onMovieClicked(adapterPosition)
        }
    }

    fun bind(movieModel: MovieModel) {
        Picasso.get().load(movieModel.imageUrl).into(ivImage)
        tvTitle.text = movieModel.name
        tvOverview.text = movieModel.overview
    }
}