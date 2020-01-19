package com.android.academy.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.db.AppDatabase
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.TrailerModel
import com.android.academy.networking.RestClient
import com.android.academy.networking.TrailersListResult
import com.android.academy.threads.AppExecutors
import com.android.academy.utils.logD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailersRepository(private val appContext: Context) {

    private val mutableLiveData: MutableLiveData<TrailerModel> by lazy { MutableLiveData<TrailerModel>() }

    fun getTrailer(): LiveData<TrailerModel> = mutableLiveData

    fun fetchTrailer(movieModel: MovieModel) {
        AppExecutors.diskIO.execute {
            val trailerModel = AppDatabase.getInstance(appContext)?.videoDao()?.getVideo(movieModel.movieId)
            if (trailerModel != null) {
                logD("We got trailer from DB")
                mutableLiveData.postValue(trailerModel)
            } else {
                logD("We didn't get trailer from DB")
                getTrailerFromServer(movieModel)
            }
        }
    }

    private fun getTrailerFromServer(movieModel: MovieModel) {
        RestClient.moviesService.getTrailers(movieModel.movieId).enqueue(object :
            Callback<TrailersListResult> {
            override fun onFailure(call: Call<TrailersListResult>, t: Throwable) {
                logD("On failure: ${t.message}")
                mutableLiveData.postValue(null)
            }

            override fun onResponse(call: Call<TrailersListResult>, response: Response<TrailersListResult>) {
                logD("On response from server called")
                response.body()?.let {
                    logD("We got ${response.body()!!.results.size} trailers")
                    val trailerModel = MovieModelConverter.convertTrailerResult(it)
                    mutableLiveData.postValue(trailerModel)
                    updateDbWithNewTrailer(trailerModel)
                }
            }

        })
    }

    private fun updateDbWithNewTrailer(trailerModel: TrailerModel?) {
        logD("Update DB with trailer")
        AppExecutors.diskIO.execute {
            AppDatabase.getInstance(appContext)?.videoDao()?.insert(trailerModel)
        }
    }
}