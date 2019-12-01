package com.android.academy.networking

import com.android.academy.networking.NetworkingConstants.BASE_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    val moviesService by lazy {
        val retrofit = createRetrofitClient()

        retrofit.create(MoviesService::class.java)
    }

    private fun createRetrofitClient(): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}