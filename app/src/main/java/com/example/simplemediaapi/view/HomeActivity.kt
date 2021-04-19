package com.example.simplemediaapi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.simplemediaapi.R
import com.example.simplemediaapi.utils.NetworkConnection
import com.example.simplemediaapi.viewmodel.NetworkViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    private val networkViewModel by lazy { ViewModelProvider(this).get(NetworkViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController)
        toolbar.setupWithNavController(navController)

        /**
         * Отслеживание состояния сети и изменения интерфейса при отключении/подключении сети.
         */
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isNetworkConnected ->
            networkViewModel.networkConnectionChanged(isNetworkConnected)
            if (isNetworkConnected) {
                this.toolbar.title = navController.currentDestination?.label
            } else {
                this.toolbar.title = getString(R.string.waitingNetworkConnection)
            }
        })
    }
}