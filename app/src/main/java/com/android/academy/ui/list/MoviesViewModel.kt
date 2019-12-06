package com.android.academy.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.model.MovieModel
import com.android.academy.repos.MoviesRepository

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val moviesRepository = MoviesRepository(application)

    private val movies: MutableLiveData<List<MovieModel>> by lazy {
        MutableLiveData<List<MovieModel>>().also {
            loadMovies()
        }
    }

    fun getMovies(): LiveData<List<MovieModel>> {
        return movies
    }

    private fun loadMovies() {
        moviesRepository.getMovies().observeForever {
            it?.let { movies.postValue(it) }
        }
    }
}