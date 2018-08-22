package com.kanch786.musicapp.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


val baseUrl  = "http://itunes.apple.com"
interface  ApiInterface {

    @GET("search/")
    fun getSongResultResponse(@Query("term") term : String, @Query("limit") limit : Int)


    companion object Factory {
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}