package com.example.mediastore.net

import com.example.mediastore.entity.ImageEntity
import retrofit2.Call
import retrofit2.http.GET

interface MainService  {
    @GET("v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    fun getImage(): Call<ImageEntity>

}