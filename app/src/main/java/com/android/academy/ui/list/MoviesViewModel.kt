package com.android.academy.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.academy.model.MovieModel
import com.android.academy.repos.MoviesRepository

class MoviesViewModel : ViewModel() {

    private val moviesRepository = MoviesRepository()

    private val movies: MutableLiveData<List<MovieModel>> by lazy {
        MutableLiveData<List<MovieModel>>().also {
            loadMovies()
        }
    }

    fun getMovies(): LiveData<List<MovieModel>> {
        return movies
    }

    private fun loadMovies() {
        moviesRepository.getMoviesFromServer().observeForever {
            it?.let { movies.postValue(it) }
        }
    }
}