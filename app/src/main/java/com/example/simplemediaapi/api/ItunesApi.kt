package com.example.simplemediaapi.api

import com.example.simplemediaapi.model.AlbumResponse
import com.example.simplemediaapi.model.AlbumsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Интерфейс для выполнения запросов к Api.
 */
interface ITunesApi {

    @GET("/search")
    fun getAlbums(
        @Query("term") searchText: String,
        @Query("entity") typeOfContent: String,
        @Query("attribute") searchingAttribute: String,
        @Query("limit") limit: String,
        @Query("country") country: String
    ): Call<AlbumsResponse>

    @GET("/search")
    fun getSongs(
        @Query("term") searchText: String,
        @Query("entity") typeOfContent: String,
        @Query("limit") limit: String,
        @Query("country") country: String
    ): Call<AlbumResponse>
}
