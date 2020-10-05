package com.example.simplemediaapi.model

import com.example.simplemediaapi.api.ApiService
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

/**
 * Синглтон для выполнения запросов Api.
 */
object ITunesRepository {

    /**
     * Получение альбомов из Api.
     * @param searchText запрос для поиска
     * @param typeOfContent тип запрашиваемого контента
     * @param searchingAttribute контект, в котором нужно исказать, по запросу
     * @param limit количество записей
     * @param country страна
     */
    fun getAlbums(
        searchText: String,
        typeOfContent: String,
        searchingAttribute: String,
        limit: String,
        country: String
    ): List<Album> {
        val call: Call<AlbumsResponse> = ApiService.iTunesApi.getAlbums(
            searchText, typeOfContent, searchingAttribute, limit, country
        )
        return try {
            val response: Response<AlbumsResponse> = call.execute()
            response.body()?.results ?: listOf()
        } catch (e: IOException) {
            listOf()
        }

    }

    /**
     * Получение песен из Api.
     * @param searchText запрос для поиска
     * @param typeOfContent тип запрашиваемого контента
     * @param limit количество записей
     * @param country страна
     */
    fun getSongs(
        searchText: String,
        typeOfContent: String,
        limit: String,
        country: String
    ): List<Song> {
        val call: Call<AlbumResponse> = ApiService.iTunesApi.getSongs(
            searchText, typeOfContent, limit, country
        )
        return try {
            val response: Response<AlbumResponse> = call.execute()
            response.body()?.results ?: listOf()
        } catch (e: IOException) {
            listOf()
        }

    }

}