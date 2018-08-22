package com.kanch786.musicapp.api

import android.util.Log
import io.reactivex.Observable
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit


val baseUrl  = "http://itunes.apple.com/"
interface  ApiInterface {

    @GET("search")
    fun getSongResultResponse(@Query("term") term : String, @Query("limit") limit : Int)
    :Observable<Response<SongListResponse>>


    companion object Factory {
        fun create(): ApiInterface {

            val okHttpClient = OkHttpClient.Builder()
                   // .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT,spec))
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(
                            HttpLoggingInterceptor(
                                    HttpLoggingInterceptor.Logger {
                                        Log.i("Music Service", ": $it")
                                    }
                            ).setLevel(HttpLoggingInterceptor.Level.BASIC)).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}