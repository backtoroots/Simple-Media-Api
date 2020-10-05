package com.example.simplemediaapi.constants

/**
 * Константы для работы с Api и данными, полученными из Api.
 */
class ApiConstants {
    companion object {
        @JvmStatic val baseUrl = "https://itunes.apple.com/"
        @JvmStatic val albumTypeOfContent = "album"
        @JvmStatic val songTypeOfContent = "song"
        @JvmStatic val searchByAlbum = "albumTerm"
        @JvmStatic val limitForAlbums = "15"
        @JvmStatic val limitForSongs = "500"
        @JvmStatic val countryRu = "ru"
    }
}