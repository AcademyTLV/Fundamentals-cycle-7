package com.android.academy.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.model.MovieModel
import com.android.academy.repos.MoviesRepository

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val moviesRepository = MoviesRepository(application)

    private val movies: MutableLiveData<List<MovieModel>> by lazy {
        MutableLiveData<List<MovieModel>>().also {
            getMoviesFromDb()
        }
    }

    fun getMovies(): LiveData<List<MovieModel>> = movies

    private fun getMoviesFromDb() {
        moviesRepository.getMoviesFromDb().observeForever {
            it?.let { movies.postValue(it) }
        }
    }

}