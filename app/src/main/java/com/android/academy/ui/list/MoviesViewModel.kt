package com.android.academy.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.academy.model.MovieModel
import com.android.academy.repos.MoviesRepository

enum class State { LOADING, LOADED, ERROR }

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val moviesRepository = MoviesRepository(application)

    // Movies
    private val movies: MutableLiveData<List<MovieModel>> by lazy {
        MutableLiveData<List<MovieModel>>().also {
            state.postValue(State.LOADING)
            loadMovies()
        }
    }

    fun getMovies(): LiveData<List<MovieModel>> = movies


    // State
    private val state: MutableLiveData<State> by lazy { MutableLiveData<State>() }

    fun getState(): LiveData<State> = state

    // Open details
    private val openDetails: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun getOpenDetails(): LiveData<Int> = openDetails


    private fun loadMovies() {
        moviesRepository.getMovies().observeForever {
            if (it == null) {
                state.postValue(State.ERROR)
                return@observeForever
            }

            if (it.isNotEmpty()) {
                state.postValue(State.LOADED)
            }
            movies.postValue(it)
        }
    }

    fun clearMoviesFromDb() {
        moviesRepository.clearMoviesFromDb()
    }

    fun onMovieClicked(adapterPosition: Int) {
        openDetails.postValue(adapterPosition)
    }

    override fun onCleared() {
        super.onCleared()
    }
}