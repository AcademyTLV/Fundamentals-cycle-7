package com.android.academy.repos

import androidx.lifecycle.MutableLiveData
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository {

    val mutableLiveData = MutableLiveData<List<MovieModel>>()

    fun getMovies(): MutableLiveData<List<MovieModel>> {
        RestClient.moviesService.loadPopularMovies().enqueue(object : Callback<MoviesListResult> {
            override fun onFailure(call: Call<MoviesListResult>, t: Throwable) {
            }

            override fun onResponse(call: Call<MoviesListResult>, response: Response<MoviesListResult>) {
                response.body()?.let {
                    mutableLiveData.value = MovieModelConverter.convertNetworkMovieToModel(it)
                }
            }
        })
        return mutableLiveData
    }
}