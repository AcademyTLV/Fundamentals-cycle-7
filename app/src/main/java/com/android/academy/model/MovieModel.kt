package com.android.academy.model

import android.os.Parcelable
import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.NetworkingConstants.BACKDROP_BASE_URL
import com.android.academy.networking.NetworkingConstants.POSTER_BASE_URL
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieModel(
    val movieId: Int,
    val name: String,
    val imageUrl: String,
    val overview: String?,
    val backImageUrl: String,
    val releaseDate: String
) : Parcelable

object MovieModelConverter {

    fun convertNetworkMovieToModel(model : MoviesListResult) : List<MovieModel> {
        return model.results.map {
            MovieModel(
                movieId = it.id,
                name = it.title,
                imageUrl = "${POSTER_BASE_URL}${it.posterPath}",
                overview = it.overview,
                backImageUrl = "${BACKDROP_BASE_URL}${it.backdropPath}",
                releaseDate = it.release_date
            )
        }
    }
}