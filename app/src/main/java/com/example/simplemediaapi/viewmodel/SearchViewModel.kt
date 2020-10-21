package com.example.simplemediaapi.viewmodel

import androidx.lifecycle.*
import com.example.simplemediaapi.R
import com.example.simplemediaapi.adapters.AlbumsRecyclerViewAdapter
import com.example.simplemediaapi.constants.ApiConstants
import com.example.simplemediaapi.model.ITunesRepository
import com.example.simplemediaapi.model.Album
import com.xyarim.users.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel для SearchActivity.
 */
class SearchViewModel : ViewModel() {

    private val _toStart = MutableLiveData<Event<Album>>()
    val toStart: LiveData<Event<Album>> = _toStart

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
            Event(
                Album(
                    albums[position].id,
                    albums[position].artistName,
                    albums[position].name,
                    albums[position].imageUrl
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