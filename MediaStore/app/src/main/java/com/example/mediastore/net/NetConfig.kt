package com.example.mediastore.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetConfig {
    private const val baseUrl: String =
        "http://service.picasso.adesk.com/"
    private val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> getService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}