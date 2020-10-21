package com.example.simplemediaapi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.simplemediaapi.R
import com.example.simplemediaapi.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController)
        toolbar.setupWithNavController(navController)

        /**
         * Отслеживание состояния сети и изменения интерфейса при отключении/подключении сети.
         */
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isConnected ->
            if (isConnected) {
                this.toolbar.title = navController.currentDestination?.label
            } else {
                this.toolbar.title = "Ожидание сетевого подключения"
            }
        })
    }

}