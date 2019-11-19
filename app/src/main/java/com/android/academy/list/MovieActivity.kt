package com.android.academy.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.R
import com.android.academy.details.DetailsActivity
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.MoviesContent.movies
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.RestClient
import kotlinx.android.synthetic.main.activity_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivity : AppCompatActivity(), OnMovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        loadMovies()
        with(movies_rv_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MoviesActivity)
            adapter = MoviesViewAdapter(movies, this@MoviesActivity, this@MoviesActivity)
        }
    }

    override fun onMovieClicked(itemPosition: Int) {
        if (itemPosition < 0 || itemPosition >= movies.size) return

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, itemPosition)
        startActivity(intent)
    }


    private fun loadMovies() {
        RestClient.moviesService.loadPopularMovies().enqueue(object : Callback<MoviesListResult>{
            override fun onFailure(call: Call<MoviesListResult>, t: Throwable) {
                println("No")
            }

            override fun onResponse(
                call: Call<MoviesListResult>,
                response: Response<MoviesListResult>
            ) {
                response.body()?.let {
                    val convertedList =
                        MovieModelConverter.convertNetworkMovieToModel(it)
                    movies.apply {
                        clear()
                        addAll(convertedList)
                    }
                    movies_rv_list.adapter?.notifyDataSetChanged()
                    println("hi ${response.body()?.results?.size}")
                }
            }

        })

    }
}