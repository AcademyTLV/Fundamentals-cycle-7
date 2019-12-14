package com.android.academy.repos

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.db.AppDatabase
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.TrailerModel
import com.android.academy.networking.RestClient
import com.android.academy.networking.TrailersListResult
import com.android.academy.ui.list.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailersRepository(private val appContext: Context) {

    private val mutableLiveData: MutableLiveData<TrailerModel> by lazy { MutableLiveData<TrailerModel>() }

    fun getTrailer(): LiveData<TrailerModel> = mutableLiveData

    fun fetchTrailer(movieModel: MovieModel) {
        val trailerModel = AppDatabase.getInstance(appContext)?.videoDao()?.getVideo(movieModel.movieId)
        if (trailerModel != null) {
            Log.d(TAG, "We got trailer from DB")
            mutableLiveData.value = trailerModel
        } else {
            Log.d(TAG, "We didn't get trailer from DB")
            getTrailerFromServer(movieModel)
        }
    }

    private fun getTrailerFromServer(movieModel: MovieModel) {
        RestClient.moviesService.getTrailers(movieModel.movieId).enqueue(object :
            Callback<TrailersListResult> {
            override fun onFailure(call: Call<TrailersListResult>, t: Throwable) {
                Log.d(TAG, "On failure: ${t.message}")
                mutableLiveData.value = null
            }

            override fun onResponse(call: Call<TrailersListResult>, response: Response<TrailersListResult>) {
                Log.d(TAG, "On response from server called")
                response.body()?.let {
                    Log.d(TAG, "We got ${response.body()!!.results.size} trailers")
                    val trailerModel = MovieModelConverter.convertTrailerResult(it)
                    mutableLiveData.value = trailerModel
                    updateDbWithNewTrailer(trailerModel)
                }
            }

        })
    }

    private fun updateDbWithNewTrailer(trailerModel: TrailerModel?) {
        Log.d(TAG, "Update DB with trailer")
        AppDatabase.getInstance(appContext)?.videoDao()?.insert(trailerModel)
    }
}