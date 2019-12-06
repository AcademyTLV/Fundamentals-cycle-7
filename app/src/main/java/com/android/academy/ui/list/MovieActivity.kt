package com.android.academy.ui.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.R
import com.android.academy.db.AppDatabase
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.MoviesContent
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.RestClient
import com.android.academy.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.activity_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "MOVIESX"

class MoviesActivity : AppCompatActivity(), OnMovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        loadMovies()
        setRecyclerView()

        getViewModelAndObserve()
    }

    private fun getViewModelAndObserve() {
        val model = ViewModelProviders.of(this)[MoviesViewModel::class.java]
        model.getMovies().observe(this, Observer<List<MovieModel>> { movies ->
            Log.d(TAG, "We got fresh ${movies.size} movies")
            main_progress.visibility = View.GONE

            if (movies.isEmpty()) {
                onFailGettingDataFromServer()
                return@Observer
            }

            onFreshMoviesReceived(movies)
        })
    }

    private fun onFailGettingDataFromServer() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
    }

    private fun onFreshMoviesReceived(movies: List<MovieModel>) {
        MoviesContent.movies.apply {
            clear()
            addAll(movies)
        }
        movies_rv_list.adapter?.notifyDataSetChanged()

        updateDbWithNewMovies()
    }

    private fun loadMovies() {
        MoviesContent.clear()
        getCachedMoviesFromDataBase()
        main_progress.visibility = View.VISIBLE
    }

    private fun setRecyclerView() {
        movies_rv_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MoviesViewAdapter(
                MoviesContent.movies,
                this@MoviesActivity,
                context
            )
        }
    }

    private fun getCachedMoviesFromDataBase() {
        val cachedMovies: List<MovieModel>? = AppDatabase.getInstance(this)?.movieDao()?.getAll()
        cachedMovies?.let {
            Log.d(TAG, "We got cached ${it.size} movies")

            MoviesContent.movies.addAll(cachedMovies)
            movies_rv_list.adapter?.notifyDataSetChanged()
        }
    }

    private fun updateDbWithNewMovies() {
        AppDatabase.getInstance(this)?.apply {
            movieDao()?.deleteAll()
            movieDao()?.insertAll(MoviesContent.movies)
            Log.d(TAG, "DB Updated with fresh movies")
        }
    }


    // OnMovieClickListener interface
    override fun onMovieClicked(itemPosition: Int) {
        if (itemPosition < 0 || itemPosition >= MoviesContent.movies.size) return
        startDetailsActivity(itemPosition)
    }

    private fun startDetailsActivity(itemPosition: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, itemPosition)
        startActivity(intent)
    }

    // Menu functions
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AppDatabase.getInstance(this.applicationContext)?.movieDao()?.deleteAll()
                (movies_rv_list.adapter as MoviesViewAdapter).clearData()
            }
        }
        return true
    }
}