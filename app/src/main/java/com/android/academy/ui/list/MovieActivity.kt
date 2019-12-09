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
import com.android.academy.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.activity_movies.*

const val TAG = "MOVIESX"

class MoviesActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var moviesAdapter: MoviesViewAdapter

    private val moviesViewModel: MoviesViewModel
        get() = ViewModelProviders.of(this)[MoviesViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        initRecyclerView()
        getMovies()
    }

    private fun initRecyclerView() {
        moviesList.layoutManager = LinearLayoutManager(this)

        // Create Movies Adapter
        moviesAdapter = MoviesViewAdapter(this, this)

        // Attach Adapter to RecyclerView
        moviesList.adapter = moviesAdapter

        // Populate Adapter with data
        //moviesAdapter.setData(movies)

    }

    private fun getMovies() {
        Log.d(TAG, "getMovies called")
        main_progress.visibility = View.VISIBLE

        moviesViewModel.getMovies().observe(this, Observer<List<MovieModel>> { movies ->
            main_progress.visibility = View.GONE

            if (movies.isEmpty()) {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                return@Observer
            }

            onFreshMoviesReceived(movies)
        })
    }

    private fun onFreshMoviesReceived(movies: List<MovieModel>) {
        moviesAdapter.setData(movies)
        moviesList.adapter?.notifyDataSetChanged()

        updateDbWithNewMovies(movies)
    }

    private fun updateDbWithNewMovies(movies: List<MovieModel>) {
        AppDatabase.getInstance(this)?.apply {
            movieDao()?.deleteAll()
            movieDao()?.insertAll(movies)
            Log.d(TAG, "DB Updated with fresh movies")
        }
    }

    // OnMovieClickListener interface
    override fun onMovieClicked(movie: MovieModel) {
        Toast.makeText(this, movie.name, Toast.LENGTH_SHORT).show()
    }

    private fun startDetailsActivity(itemPosition: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, itemPosition)
        startActivity(intent)
    }

    // Menu functions
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movies_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AppDatabase.getInstance(this.applicationContext)?.movieDao()?.deleteAll()
                moviesAdapter.setData(mutableListOf())
            }
        }
        return true
    }
}