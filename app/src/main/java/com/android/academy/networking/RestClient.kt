package com.android.academy.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    val moviesService by lazy {
        val retrofit = Retrofit.Builder().baseUrl(MoviesService.BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MoviesService::class.java)
    }
}