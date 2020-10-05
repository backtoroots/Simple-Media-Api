package com.example.simplemediaapi.viewmodel

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.example.simplemediaapi.view.AlbumActivity
import com.example.simplemediaapi.R
import com.example.simplemediaapi.adapters.AlbumsRecyclerViewAdapter
import com.example.simplemediaapi.constants.ApiConstants
import com.example.simplemediaapi.constants.IntentConstants
import com.example.simplemediaapi.model.ITunesRepository
import com.example.simplemediaapi.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlin.reflect.KClass
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel для SearchActivity.
 */
class SearchViewModel : ViewModel() {
//    Переменная для привязки перехода к необходимой активити из ViewModel
    private val _toStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val toStart: LiveData<Pair<KClass<*>, Bundle?>> = _toStart

//    Список альбомов
    var albums: List<Album> = listOf()

//    Инициализация адаптера, хранящего записи об альбомах
    val adapter = AlbumsRecyclerViewAdapter(R.layout.albums_list_item, this)

    /**
     * Метод обрабатывает клик по одному из альбомов - элементу RecyclerView.
     * Переменная toStart, наблюдаемая из активити, заполняется классом активити, к которой нужно
     * перейти, и необходимыми для старта новой активити данными.
     * @param position номер элемента, по которому кликнул пользователь
     */
    fun clickAlbum(position: Int) {
        _toStart.postValue(
            Pair(
                AlbumActivity::class,
                bundleOf(
                    IntentConstants.idAlbum to albums[position].id,
                    IntentConstants.artistName to albums[position].artistName,
                    IntentConstants.albumName to albums[position].name,
                    IntentConstants.albumImageUrl to albums[position].imageUrl
                )
            )
        )
    }

    /**
     * Метод обрабатывает событие изменение текста в строке поиска.
     * С помощью корутины запрашиваются данные из Api.
     * В шаблоне привязывается к уже сгенерированному BindingAdapter.
     * Альбомы сортируются по имени.
     * @param searchText новый текст в строке поиска
     */
    fun onTextChanged(searchText: CharSequence, start: Int, before: Int, count: Int) {
        if (searchText.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                val albumFromApi = ITunesRepository.getAlbums(
                    searchText.toString(),
                    ApiConstants.albumTypeOfContent,
                    ApiConstants.searchByAlbum,
                    ApiConstants.limitForAlbums,
                    ApiConstants.countryRu
                )
                withContext(Dispatchers.Main) {
                    albums = albumFromApi.sortedBy { it.name }
                    adapter.notifyDataSetChanged()
                }
            }
        } else {
            albums = listOf()
            adapter.notifyDataSetChanged()
        }
    }


}