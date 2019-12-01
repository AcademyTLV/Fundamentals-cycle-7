package com.android.academy.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.R
import com.android.academy.model.MovieModel
import com.squareup.picasso.Picasso

class MoviesViewAdapter(
    private val movies: MutableList<MovieModel>,
    private val movieClickListener: OnMovieClickListener?,
    context: Context
) : RecyclerView.Adapter<MoviesViewAdapter.ViewHolder>() {

    val picasso: Picasso = Picasso.get()

    private val mLayoutInflater: LayoutInflater = context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mLayoutInflater.inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val ivImage: ImageView = view.findViewById(R.id.item_movie_iv)
        private val tvTitle: TextView = view.findViewById(R.id.item_movie_tv_title)
        private val tvOverview: TextView = view.findViewById(R.id.item_movie_tv_overview)

        init {
            view.setOnClickListener(this)
        }

        fun bind(movieModel: MovieModel) {
            picasso.load(movieModel.imageUrl).into(ivImage)
            tvTitle.text = movieModel.name
            tvOverview.text = movieModel.overview
        }

        override fun onClick(view: View) {
            movieClickListener?.onMovieClicked(adapterPosition)
        }
    }

    fun clearData() {
        movies.clear()
        notifyDataSetChanged()
    }
}