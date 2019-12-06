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

    val mutableLiveData = MutableLiveData<List<MovieModel>>()

    fun getMovies(): MutableLiveData<List<MovieModel>> {
        getMoviesFromDataBase()
        getMoviesFromServer()
        return mutableLiveData
    }

    private fun getMoviesFromDataBase() {
        val cachedMovies: List<MovieModel>? = AppDatabase.getInstance(appContext)?.movieDao()?.getAll()
        cachedMovies?.let {
            Log.d(TAG, "We got cached ${it.size} movies")
            if ( it.isNotEmpty()) {
                mutableLiveData.value = it
            }
        }
    }

    private fun getMoviesFromServer() {
        RestClient.moviesService.loadPopularMovies().enqueue(object : Callback<MoviesListResult> {
            override fun onFailure(call: Call<MoviesListResult>, t: Throwable) {
                mutableLiveData.value = listOf()
            }

            override fun onResponse(call: Call<MoviesListResult>, response: Response<MoviesListResult>) {
                Log.d(TAG, "On response from server called")
                response.body()?.let {
                    Log.d(TAG, "We got fresh ${response.body()!!.results.size} movies")
                    mutableLiveData.value = MovieModelConverter.convertNetworkMovieToModel(it)
                }
            }
        })
    }
}