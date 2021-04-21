package com.example.simplemediaapi.api

import com.example.simplemediaapi.model.Album
import com.example.simplemediaapi.model.AlbumResponse
import com.example.simplemediaapi.model.AlbumsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
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
        @Query("country") country: String
    ): Single<Response<AlbumsResponse>>

    @GET("/search")
    fun getSongs(
        @Query("term") searchText: String,
        @Query("entity") typeOfContent: String,
        @Query("country") country: String
    ): Single<Response<AlbumResponse>>
}
