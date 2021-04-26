package com.example.simplemediaapi.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> Fragment.buildViewModel(crossinline viewModelFactory: () -> T): T {
    return ViewModelProvider(this, object : ViewModelProvider.NewInstanceFactory() {
        override fun <A : ViewModel?> create(modelClass: Class<A>): A {
            return viewModelFactory() as A
        }
    }).get(T::class.java)
}
