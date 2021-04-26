package com.example.simplemediaapi.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.simplemediaapi.R
import com.example.simplemediaapi.databinding.ActivityHomeBinding
import com.example.simplemediaapi.utils.NetworkConnection
import com.example.simplemediaapi.viewmodel.NetworkViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val networkViewModel by lazy { ViewModelProvider(this).get(NetworkViewModel::class.java) }
    private lateinit var binding: ActivityHomeBinding
    private var destinationChanged: PublishSubject<String> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        Observable.combineLatest(
            destinationChanged,
            networkViewModel.networkConnectionChanges
        ) { destinationClassName, isNetworkConnected ->
            destinationClassName to isNetworkConnected }
            .subscribe { (destinationClassName, isNetworkConnected) ->
                Log.v("HomeActivitySpecialTag", "combineLatest: $isNetworkConnected")
                if (isNetworkConnected) {
                    binding.toolbar.title = getCurrentTitle(destinationClassName)
                } else {
                    binding.toolbar.title = getString(R.string.waitingNetworkConnection)
                }
            }.addTo(disposables)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.v("HomeActivitySpecialTag", destination.toString())
            destinationChanged.onNext((destination as FragmentNavigator.Destination).className)
        }
        setupActionBarWithNavController(navController)
        binding.toolbar.setupWithNavController(navController)

        /**
         * Отслеживание состояния сети и изменения интерфейса при отключении/подключении сети.
         */
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isNetworkConnected ->
            networkViewModel.networkConnectionChanged(isNetworkConnected)
        })
    }

    private fun getCurrentTitle(fragmentName: String): String {
        return when(fragmentName) {
            SearchFragment::class.qualifiedName -> getString(R.string.searchFragmentLabel)
            AlbumFragment::class.qualifiedName -> getString(R.string.albumFragmentLabel)
            else -> getString(R.string.searchFragmentLabel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}