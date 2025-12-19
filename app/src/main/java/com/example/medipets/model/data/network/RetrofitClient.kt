package com.example.medipets.model.data.network

import com.example.medipets.BuildConfig
import com.example.medipets.model.data.network.service.MascotaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private val BASE_URL = BuildConfig.BACKEND_URL

    val instance: MascotaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MascotaApiService::class.java)
    }
}