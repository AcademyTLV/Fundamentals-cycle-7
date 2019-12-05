package com.android.academy.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.R
import com.android.academy.background_services.BGServiceActivity
import com.android.academy.background_services.WorkerActivity
import com.android.academy.db.AppDatabase
import com.android.academy.details.DetailsActivity
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.MoviesContent.movies
import com.android.academy.model.MoviesContent
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.RestClient
import com.android.academy.threads.AsyncTaskActivity
import com.android.academy.threads.ThreadsActivity
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
            adapter = MoviesViewAdapter(
                MoviesContent.movies,
                this@MoviesActivity,
                this@MoviesActivity
            )
        }
    }

    override fun onMovieClicked(itemPosition: Int) {
        if (itemPosition < 0 || itemPosition >= MoviesContent.movies.size) return

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, itemPosition)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.movies_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open_async_task -> {
                // Open Async Task Activity
                startActivity(Intent(this@MoviesActivity, AsyncTaskActivity::class.java))
                return true
            }

            R.id.action_open_thread_handler -> {
                // Open Thread Handler Activity
                startActivity(Intent(this@MoviesActivity, ThreadsActivity::class.java))
                return true
            }

            R.id.action_open_background_service_activity -> {
                // Open Thread Handler Activity
                startActivity(Intent(this@MoviesActivity, BGServiceActivity::class.java))
                return true
            }

            R.id.action_open_worker_activity -> {
                // Open Work Manager Activity
                WorkerActivity.open(this@MoviesActivity)
                return true
            }

            R.id.action_delete -> {
                AppDatabase.getInstance(this.applicationContext)?.movieDao()?.deleteAll()
                (movies_rv_list.adapter as MoviesViewAdapter).clearData()
                return true
            }

            R.id.action_delete -> {
                AppDatabase.getInstance(this.applicationContext)?.movieDao()?.deleteAll()
                (movies_rv_list.adapter as MoviesViewAdapter).clearData()
                return true
            }
        }
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item)
    }

    private fun loadMovies() {
        MoviesContent.clear()
        getCachedMoviesFromDataBase()
        main_progress.visibility = View.VISIBLE
        getFreshMoviesFromServer()
    }

    private fun getCachedMoviesFromDataBase() {
        val cachedMovies: List<MovieModel>? = AppDatabase.getInstance(this)?.movieDao()?.getAll()
        cachedMovies?.let {
            MoviesContent.movies.addAll(cachedMovies)
            movies_rv_list.adapter?.notifyDataSetChanged()
        }
    }

    private fun getFreshMoviesFromServer() {
        RestClient.moviesService.loadPopularMovies().enqueue(object : Callback<MoviesListResult> {
            override fun onFailure(call: Call<MoviesListResult>, t: Throwable) {
                onFailGettingDataFromServer()
            }

            override fun onResponse(
                call: Call<MoviesListResult>,
                response: Response<MoviesListResult>
            ) {
                onDataReceivedFromServer(response)
            }

        })
    }

    private fun onDataReceivedFromServer(response: Response<MoviesListResult>) {
        main_progress.visibility = View.GONE
        response.body()?.let {
            val convertedList = MovieModelConverter.convertNetworkMovieToModel(it)
            MoviesContent.movies.apply {
                clear()
                addAll(convertedList)
            }
            movies_rv_list.adapter?.notifyDataSetChanged()
            AppDatabase.getInstance(this@MoviesActivity)?.movieDao()?.deleteAll()
            AppDatabase.getInstance(this@MoviesActivity)?.movieDao()?.insertAll(MoviesContent.movies)
        }
    }

    private fun onFailGettingDataFromServer() {
        main_progress.visibility = View.GONE
        Toast.makeText(
            this@MoviesActivity,
            R.string.something_went_wrong,
            Toast.LENGTH_SHORT
        ).show()
    }

}