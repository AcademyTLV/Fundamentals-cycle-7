package com.android.academy.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.R
import com.android.academy.ui.details.DetailsActivity
import com.android.academy.utils.logD
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesActivity : AppCompatActivity() {

    private lateinit var moviesAdapter: MoviesViewAdapter

    private val moviesViewModel: MoviesViewModel
        get() = ViewModelProviders.of(this)[MoviesViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        initRecyclerView()
        observeState()
        observeMovies()
        observerOpenDetails()
    }

    private fun initRecyclerView() {
        moviesList.layoutManager = LinearLayoutManager(this)

        // Create Movies Adapter
        moviesAdapter = MoviesViewAdapter(moviesViewModel, this)

        // Attach Adapter to RecyclerView
        moviesList.adapter = moviesAdapter
    }

    private fun observeState() {
        moviesViewModel.getState().observe(this, Observer {
            if (it == null) return@Observer

            logD("State: ${it.name}")
            when (it) {
                State.LOADING -> mainProgress.visibility = View.VISIBLE
                State.LOADED -> mainProgress.visibility = View.GONE
                State.ERROR -> {
                    mainProgress.visibility = View.GONE
                    Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeMovies() {
        logD("getMovies called")
        moviesViewModel.getMovies().observe(this, Observer {
            moviesAdapter.setData(it)
        })
    }

    private fun observerOpenDetails() {
        moviesViewModel.getOpenDetails().observe(this, Observer {
            logD("open details called")
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, it)
            startActivity(intent)
        })
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