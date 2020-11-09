package com.example.simplemediaapi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Класс используется для корректного получения данных из Api.
 * @param resultsCount количество найденных записей
 * @param results найденные записи (альбомы)
 */
data class AlbumsResponse(val resultsCount: Int, val results: List<Album>)

/**
 * Класс для хранения информации об альбоме.
 * @param id ID альбома
 * @param artistName имя исполнителя
 * @param name название альбома
 * @param imageUrl url картинки альбома
 */
@Parcelize
data class Album(
    @SerializedName("collectionId") val id: Int,
    val artistName: String,
    @SerializedName("collectionName") val name: String,
    @SerializedName("artworkUrl100") val imageUrl: String
): Parcelable