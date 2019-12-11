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
        getState()
        getMovies()
    }

    private fun initRecyclerView() {
        moviesList.layoutManager = LinearLayoutManager(this)

        // Create Movies Adapter
        moviesAdapter = MoviesViewAdapter(this, this)

        // Attach Adapter to RecyclerView
        moviesList.adapter = moviesAdapter
    }

    private fun getState() {
        moviesViewModel.getState().observe(this, Observer {
            if (it == null) return@Observer

            Log.d(TAG, "State: ${it.name}")
            when(it) {
                State.LOADING -> mainProgress.visibility = View.VISIBLE
                State.LOADED -> mainProgress.visibility = View.GONE
                State.ERROR ->   Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMovies() {
        Log.d(TAG, "getMovies called")
        moviesViewModel.getMovies().observe(this, Observer<List<MovieModel>> {
            moviesAdapter.setData(it)
        })
    }

    // OnMovieClickListener interface
    override fun onMovieClicked(movie: MovieModel, adapterPosition: Int) {
        Toast.makeText(this, movie.name, Toast.LENGTH_SHORT).show()
        startDetailsActivity(adapterPosition)
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
            R.id.action_delete -> moviesViewModel.clearMoviesFromDb()
        }
        return true
    }
}