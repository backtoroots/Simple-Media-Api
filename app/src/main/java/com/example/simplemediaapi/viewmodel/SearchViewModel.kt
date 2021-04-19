package com.example.simplemediaapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.simplemediaapi.R
import com.example.simplemediaapi.adapters.AlbumsRecyclerViewAdapter
import com.example.simplemediaapi.constants.ApiConstants
import com.example.simplemediaapi.model.Album
import com.example.simplemediaapi.model.ITunesRepository
import com.example.simplemediaapi.utils.Event
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * ViewModel для SearchActivity.
 */
class SearchViewModel : ViewModel() {

    private val TAG = "SearchViewModelTag"

    private val _toStart = MutableLiveData<Event<Album>>()
    val toStart: LiveData<Event<Album>> = _toStart

    private val disposables = CompositeDisposable()
    private val searchTextChanges = BehaviorSubject.create<String>()
    private val networkConnectionChanges = PublishSubject.create<Boolean>()

    val adapter = AlbumsRecyclerViewAdapter(R.layout.albums_list_item, this)

    var displayedAlbums: List<Album> = listOf()
    set(value) {
        field = value
        adapter.notifyDataSetChanged()
    }

    init {
        val searchTextEvents = searchTextChanges
            .debounce(
                400,
                TimeUnit.MILLISECONDS,
                AndroidSchedulers.mainThread()
            ) // TODO think about debounce time length, delete hardcode
            .map { text -> text.trim() }
            .filter { text -> text.isNotEmpty() }


        Observable.combineLatest(
            networkConnectionChanges,
            searchTextEvents) { isNetworkConnected, searchText ->
            Pair(isNetworkConnected, searchText)
        }
            .filter { (isNetworkConnected, _) ->
                isNetworkConnected
            }
            .map { (_, searchText) -> searchText }
            .flatMapSingle { text ->
                Log.v("searchViewModelTag", "onNext - text: ${text}\n")
                getAlbums(text).subscribeOn(Schedulers.io()) // TODO add error handling - room cache?
            }
            .map { albums -> albums.sortedBy { it.name } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { albumsFromApi ->
                    Log.v("searchViewModelTag", "onNext - size: ${albumsFromApi.size}\n")
                    displayedAlbums = albumsFromApi
            }.addTo(disposables)
    }


    fun searchTextChanged(text: String) = searchTextChanges.onNext(text)
    fun networkConnectionChanged(isNetworkConnected: Boolean) {
        networkConnectionChanges.onNext(isNetworkConnected)
    }

    /**
     * Метод обрабатывает клик по одному из альбомов - элементу RecyclerView.
     * Переменная toStart, наблюдаемая из активити, заполняется классом активити, к которой нужно
     * перейти, и необходимыми для старта новой активити данными.
     * @param position номер элемента, по которому кликнул пользователь
     */
    fun clickAlbum(position: Int) {
        _toStart.postValue(
            Event(
                Album(
                    displayedAlbums[position].id,
                    displayedAlbums[position].artistName,
                    displayedAlbums[position].name,
                    displayedAlbums[position].imageUrl
                )
            )
        )
    }

    private fun getAlbums(searchText: String): Single<List<Album>> {
        return ITunesRepository.getAlbums(
            searchText,
            ApiConstants.albumTypeOfContent,
            ApiConstants.searchByAlbum,
            ApiConstants.limitForAlbums,
            ApiConstants.countryRu)
            .retryWhen { errors -> // TODO fix retry
                return@retryWhen errors.flatMap { Flowable.timer(5, TimeUnit.SECONDS) } // TODO hardcode
            }
            .onErrorReturn { emptyList<Album>() }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}