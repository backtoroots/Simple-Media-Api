package com.example.simplemediaapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplemediaapi.R
import com.example.simplemediaapi.adapters.SongsRecyclerViewAdapter
import com.example.simplemediaapi.constants.ApiConstants
import com.example.simplemediaapi.model.Album
import com.example.simplemediaapi.model.ITunesRepository
import com.example.simplemediaapi.model.Song
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * ViewModel для AlbumActivity.
 */
class AlbumViewModel(val album: Album): ViewModel() {

    private val networkConnectionChanges = PublishSubject.create<Boolean>()
    private val disposables = CompositeDisposable()

    private val _progressBarVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean> =  _progressBarVisible

    val adapter = SongsRecyclerViewAdapter(R.layout.songs_list_item, this)

    var displayedSongs: List<Song> = listOf()
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
            _songsNotEmpty.postValue(value.isNotEmpty())
        }

    private val _songsNotEmpty: MutableLiveData<Boolean> = MutableLiveData(true)
    val songsNotEmpty: LiveData<Boolean> =  _songsNotEmpty

    companion object {
        private const val TAG = "AlbumViewModelTag"
    }

    init {
        networkConnectionChanges
            .filter { isNetworkConnected ->
                Log.v(TAG, "try to get songs\n")
                isNetworkConnected }
            .take(1)
            .doOnNext {
                if (displayedSongs.isEmpty()) {
                    _progressBarVisible.postValue(true)
                    Log.v(TAG, "progressBarVisible: $progressBarVisible")
                }
            }
            .flatMapSingle {  getSongs().subscribeOn(Schedulers.io()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { songs ->
                displayedSongs = songs
                _progressBarVisible.postValue(false)
                _songsNotEmpty.postValue(true)
            }.addTo(disposables)
    }

    /**
     * Метод осуществляет получение песен из Api с помощью корутины.
     * Для предотвращения обращения к еще неинициализированной переменной альбома,
     * при необходимости выполняется ожидание.
     * Среди полученных песен, отбираются относящиеся к ожидаемому альбому.
     * Песни сортируются по номеру, пришедшему из Api для корректного отображения.
     */
    private fun getSongs(): Single<List<Song>> {
            return ITunesRepository.getSongs(
                album.name,
                ApiConstants.songTypeOfContent,
                ApiConstants.countryRu
            )
                .map { allSongs ->
                    allSongs.filter { it.albumId == album.id }.sortedBy { it.numberInAlbum }
                }
    }

    /**
     * Метод осуществляет конвертацию длительности песни в миллисекундах в строку формата 00:00.
     */
    fun convertTime(position: Int): String {
        val time = displayedSongs[position].duration / 1000
        return "%d:%02d".format(time / 60, time % 60)
    }

    fun networkConnectionChanged(isNetworkConnected: Boolean) {
        networkConnectionChanges.onNext(isNetworkConnected)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}