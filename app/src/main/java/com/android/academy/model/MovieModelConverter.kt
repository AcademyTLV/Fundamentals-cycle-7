package com.android.academy.model

import com.android.academy.networking.MoviesListResult
import com.android.academy.networking.NetworkingConstants
import com.android.academy.networking.TrailerResult
import com.android.academy.networking.TrailersListResult

object MovieModelConverter {
    fun convertNetworkMovieToModel(model : MoviesListResult) : List<MovieModel> {
        return model.results.map {
            MovieModel(
                movieId = it.id,
                name = it.title,
                imageUrl = "${NetworkingConstants.POSTER_BASE_URL}${it.posterPath}",
                overview = it.overview,
                backImageUrl = "${NetworkingConstants.BACKDROP_BASE_URL}${it.backdropPath}",
                releaseDate = it.release_date,
                popularity = it.popularity
            )
        }
    }

    fun convertVideoResult(videosListResult: TrailersListResult): VideoModel? {
        val results: List<TrailerResult> = videosListResult.results
        if (results.isNotEmpty()) {
            val videoResult: TrailerResult = results[0]
            return VideoModel(videosListResult.id, videoResult.id, videoResult.key)
        }
        return null
    }

}