package com.example.simplemediaapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplemediaapi.R
import com.example.simplemediaapi.adapters.SongsRecyclerViewAdapter
import com.example.simplemediaapi.constants.ApiConstants
import com.example.simplemediaapi.model.ITunesRepository
import com.example.simplemediaapi.model.Album
import com.example.simplemediaapi.model.Song
import kotlinx.coroutines.*

/**
 * ViewModel для AlbumActivity.
 */
class AlbumViewModel: ViewModel() {
    lateinit var album: Album

    init {
        getSongs()
    }

//    Переменная для отслеживания клика по кнопке перехода к предыдущей активити.
    private val _backPressed = MutableLiveData<Boolean>()
    val backPressed: LiveData<Boolean> = _backPressed

//    Переменная для хранения найденных песен
    var songs: List<Song> = listOf()
    set(value) {
       field = value
        _songsNotEmpty.postValue(value.isNotEmpty())
    }

//    Переменная для отслеживания отсутствия песен
    private val _songsNotEmpty: MutableLiveData<Boolean> = MutableLiveData(true)
    val songsNotEmpty: LiveData<Boolean> =  _songsNotEmpty

//    Инициализация адаптера, хранящего записи о песнях
    val adapter = SongsRecyclerViewAdapter(R.layout.songs_list_item, this)

    /**
     * Метод обрабатывает нажатие кнопки "Вернуться назад"
      */
    fun clickBack() = _backPressed.postValue(true)

    /**
     * Метод осуществляет получение песен из Api с помощью корутины.
     * Для предотвращения обращения к еще неинициализированной переменной альбома,
     * при необходимости выполняется ожидание.
     * Среди полученных песен, отбираются относящиеся к ожидаемому альбому.
     * Песни сортируются по номеру, пришедшему из Api для корректного отображения.
     */
    private fun getSongs() {
        GlobalScope.launch(Dispatchers.IO) {
            while (!this@AlbumViewModel::album.isInitialized)
                delay(100)
            val song = ITunesRepository.getSongs(
                album.name,
                ApiConstants.songTypeOfContent,
                ApiConstants.limitForSongs,
                ApiConstants.countryRu
            )
            val songOfExpectedAlbum: List<Song> = song.filter { it.albumId == album.id }

            withContext(Dispatchers.Main) {
                songs = songOfExpectedAlbum.sortedBy { it.numberInAlbum }
                adapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * Метод осуществляет конвертацию длительности песни в миллисекундах в строку формата 00:00.
     */
    fun convertTime(position: Int): String {
        val time = songs[position].duration / 1000
        return "%d:%02d".format(time / 60, time % 60)
    }


}