package com.example.simplemediaapi.model

import android.util.Log
import com.example.simplemediaapi.api.ApiService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Синглтон для выполнения запросов Api.
 */
object ITunesRepository {

    private val TAG = "ITunesApiRepositoryTAG"

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
    ): Single<List<Album>> {
       return ApiService.iTunesApi.getAlbums( // TODO add map with status codes
            searchText, typeOfContent, searchingAttribute, limit, country)
           .doOnSuccess {
                Log.v(TAG, "Response code: ${it.code()} ${it.message()}\n")
           }
           .map { it.body() }
           .map { it.results }
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
    ): Single<List<Song>> {
        return ApiService.iTunesApi.getSongs(searchText, typeOfContent, limit, country)
            .doOnSuccess {
                Log.v(TAG, "Response code: ${it.code()} ${it.message()}\n")
            }
            .map { it.body() }
            .map { it.results }
    }

}