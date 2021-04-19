package com.example.simplemediaapi.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class NetworkViewModel: ViewModel() {
    private val _networkConnectionChanges = BehaviorSubject.createDefault(false)
    val networkConnectionChanges: Observable<Boolean> = _networkConnectionChanges.hide()//.share()

    fun networkConnectionChanged(isNetworkConnected: Boolean) {
        _networkConnectionChanges.onNext(isNetworkConnected)
    }
}