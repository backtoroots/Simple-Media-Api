package com.example.simplemediaapi.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.simplemediaapi.R
import com.example.simplemediaapi.databinding.ActivitySearchBinding
import com.example.simplemediaapi.utils.NetworkConnection
import com.example.simplemediaapi.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Активити поиска альбома.
 */
class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySearchBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_search
        )

        /**
         * Отслеживает событие клика по найденному альбому.
         * При возникновении события, стартует новую активити.
         */
        viewModel.toStart.observe(this, { value ->
            val intent = Intent(this, value.first.java)
            if (value.second != null)
                intent.putExtras(value.second as Bundle)
            startActivity(intent)
        })

        /**
         * Отслеживание состояния сети и изменения интерфейса при отключении/подключении сети.
         */
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isConnected ->
            if (isConnected) {
                albumsList.visibility = View.VISIBLE
                disconnectedView.visibility = View.GONE
            } else {
                albumsList.visibility = View.GONE
                disconnectedView.visibility = View.VISIBLE
            }
        })

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

    }

}