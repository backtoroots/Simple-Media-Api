package com.example.simplemediaapi.api

import com.example.simplemediaapi.constants.ApiConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Синглтон создает объекты для работы с Api.
 */
object ApiService {
    private val BASE_URL = ApiConstants.baseUrl

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesApi: ITunesApi by lazy { retrofit.create(ITunesApi::class.java) }
}