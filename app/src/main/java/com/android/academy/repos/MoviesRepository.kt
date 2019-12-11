package com.android.academy.repos

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.academy.db.AppDatabase
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.RestClient
import com.android.academy.ui.list.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(private val appContext: Context) {

    private val mutableLiveData = MutableLiveData<List<MovieModel>>()

    fun getMovies(): MutableLiveData<List<MovieModel>> {
        getMoviesFromDataBase()
        getMoviesFromServer()
        return mutableLiveData
    }

    fun getMoviesFromDb(): MutableLiveData<List<MovieModel>> {
        getMoviesFromDataBase()
        return mutableLiveData
    }

    private fun getMoviesFromDataBase() {
        AppDatabase.getInstance(appContext)?.movieDao()?.getAll()?.observeForever {
            it?.let {
                Log.d(TAG, "We got ${it.size} movies from DB")
                mutableLiveData.value = it
            }
        }
    }

    private fun getMoviesFromServer() {
        RestClient.moviesService.loadPopularMovies().enqueue(object : Callback<MoviesListResult> {
            override fun onFailure(call: Call<MoviesListResult>, t: Throwable) {
                Log.d(TAG, "On failure: ${t.message}")
                mutableLiveData.value = null
            }

            override fun onResponse(call: Call<MoviesListResult>, response: Response<MoviesListResult>) {
                Log.d(TAG, "On response from server called")
                response.body()?.let {
                    Log.d(TAG, "We got fresh ${response.body()!!.results.size} movies")
                    updateDbWithNewMovies(MovieModelConverter.convertNetworkMovieToModel(it))
                }
            }
        })
    }

    private fun updateDbWithNewMovies(movies: List<MovieModel>) {
        Log.d(TAG, "Update DB with fresh movies")
        AppDatabase.getInstance(appContext)?.apply {
            movieDao()?.deleteAll()
            movieDao()?.insertAll(movies)
        }
    }

    fun clearMoviesFromDb() {
        Log.d(TAG, "clearMoviesFromDb")
        AppDatabase.getInstance(appContext)?.movieDao()?.deleteAll()
    }
}