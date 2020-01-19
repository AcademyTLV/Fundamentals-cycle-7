package com.android.academy.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.academy.R
import com.android.academy.background_services.BGServiceActivity
import com.android.academy.background_services.WorkerActivity
import com.android.academy.db.AppDatabase
import com.android.academy.details.DetailsActivity
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.MoviesContent
import com.android.academy.model.MoviesContent.movies
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.RestClient
import com.android.academy.threads.AppExecutors
import com.android.academy.threads.AsyncTaskActivity
import com.android.academy.threads.ThreadsActivity
import kotlinx.android.synthetic.main.activity_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var moviesAdapter: MoviesViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        loadMovies()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        moviesList.layoutManager =
            LinearLayoutManager(this@MoviesActivity) as RecyclerView.LayoutManager

        // Create Movies Adapter
        moviesAdapter = MoviesViewAdapter(
            context = this@MoviesActivity,
            movieClickListener = this@MoviesActivity
        )

        // Attach Adapter to RecyclerView
        moviesList.adapter = moviesAdapter

        moviesList.setHasFixedSize(true)
        // Populate Adapter with data
        moviesAdapter.setData(movies)
    }

    override fun onMovieClicked(movie: MovieModel, itemPosition: Int) {
        Toast.makeText(this, movie.name, Toast.LENGTH_SHORT).show()

        if (itemPosition < 0 || itemPosition >= movies.size) return
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
                AppExecutors.diskIO.execute {
                    AppDatabase.getInstance(this.applicationContext)?.movieDao()?.deleteAll()
                }
                (moviesList.adapter as MoviesViewAdapter).clearData()
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
        AppExecutors.diskIO.execute {
            val cachedMovies: List<MovieModel>? =
                AppDatabase.getInstance(this)?.movieDao()?.getAll()
            handleCachedMoviesFromDb(cachedMovies)
        }
    }

    private fun handleCachedMoviesFromDb(cachedMovies: List<MovieModel>?) {
        AppExecutors.mainThread.execute {
            cachedMovies?.let {
                movies.addAll(cachedMovies)
                moviesList.adapter?.notifyDataSetChanged()
            }
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
            movies.apply {
                clear()
                addAll(convertedList)
            }
            moviesList.adapter?.notifyDataSetChanged()
            AppExecutors.diskIO.execute {
                AppDatabase.getInstance(this@MoviesActivity)?.movieDao()?.deleteAll()
                AppDatabase.getInstance(this@MoviesActivity)?.movieDao()?.insertAll(MoviesContent.movies)
            }
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