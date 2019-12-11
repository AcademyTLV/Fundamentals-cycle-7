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

    private val movies: MutableLiveData<List<MovieModel>> by lazy {
        MutableLiveData<List<MovieModel>>().also {
            state.postValue(State.LOADING)
            loadMovies()
        }
    }

    private val state: MutableLiveData<State> by lazy {
        MutableLiveData<State>()
    }

    fun getMovies(): LiveData<List<MovieModel>> = movies

    fun getState(): LiveData<State> = state

    private fun loadMovies() {
        moviesRepository.getMovies().observeForever {
            if (it != null) {
                if (it.isNotEmpty()) {
                    state.postValue(State.LOADED)
                }
                movies.postValue(it)
            } else {
                state.postValue(State.ERROR)
            }
        }
    }

    fun clearMoviesFromDb() {
        moviesRepository.clearMoviesFromDb()
    }
}