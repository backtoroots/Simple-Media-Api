package com.example.simplemediaapi.model

import com.google.gson.annotations.SerializedName

/**
 * Класс используется для корректного получения данных из Api.
 * @param resultsCount количество найденных записей
 * @param results найденные записи (песни)
 */
data class AlbumResponse(val resultsCount: Int, val results: List<Song>)

/**
 * Класс для хранения информации о песне.
 * @param albumId ID альбома
 * @param name название песни
 * @param duration длительности песни
 * @param numberInAlbum номер песни в альбоме
 */
data class Song(
    @SerializedName("collectionId") val albumId: Int,
    @SerializedName("trackName") val name: String,
    @SerializedName("trackTimeMillis") val duration: Int,
    @SerializedName("trackNumber") val numberInAlbum: Int
) {
//  Получение номера песни строкой. Используется в шаблоне
    fun getNumber(): String = numberInAlbum.toString()
}