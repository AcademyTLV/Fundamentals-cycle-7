package com.android.academy.networking

import com.android.academy.networking.NetworkingConstants.BASE_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    val moviesService: MoviesService by lazy {
        val retrofit = createRetrofitClient()
        retrofit.create(MoviesService::class.java)
    }

    private fun createRetrofitClient(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}