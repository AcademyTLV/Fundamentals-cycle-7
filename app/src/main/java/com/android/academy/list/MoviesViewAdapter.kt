package com.android.academy.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.R
import com.android.academy.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

private class MoviesDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {

    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }
}

class MoviesViewAdapter(
    private val movieClickListener: OnMovieClickListener,
    context: Context
) : RecyclerView.Adapter<MoviesViewAdapter.ViewHolder>() {

    val picasso = Picasso.get()
    private val layoutInflater: LayoutInflater = context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val asyncListDiffer = AsyncListDiffer<MovieModel>(this, MoviesDiffUtilCallback())

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view, movieClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieModel = asyncListDiffer.currentList[position]
        holder.bind(movieModel)
    }

    fun setData(newItems: List<MovieModel>) {
        asyncListDiffer.submitList(newItems)
    }

    inner class ViewHolder(view: View, movieClickListener: OnMovieClickListener) :
        RecyclerView.ViewHolder(view) {

        private val ivImage: ImageView = view.item_movie_iv
        private val tvTitle: TextView = view.item_movie_tv_title
        private val tvOverview: TextView = view.item_movie_tv_overview

        private lateinit var movieModel: MovieModel

        init {
            view.setOnClickListener {
                movieClickListener.onMovieClicked(movieModel, adapterPosition)
            }
        }

        fun bind(movieModel: MovieModel) {
            picasso.load(movieModel.imageUrl).into(ivImage)
            tvTitle.text = movieModel.name
            tvOverview.text = movieModel.overview

            this.movieModel = movieModel
        }
    }
}