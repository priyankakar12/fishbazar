package com.pk.fishmarket.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
       // private const val BASE_URL = "http://gastronics.hierodev.com/api/"
    private const val LIVE_URL = "http://freshfishbazar.com/Fishbazar/index.php/"

    private val retrofitClient: Retrofit.Builder by lazy {

        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(logger)

        Retrofit.Builder()
            .baseUrl(LIVE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttp.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}