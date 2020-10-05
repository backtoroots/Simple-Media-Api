package com.example.simplemediaapi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.simplemediaapi.R
import com.example.simplemediaapi.constants.IntentConstants
import com.example.simplemediaapi.databinding.ActivityAlbumBinding
import com.example.simplemediaapi.model.Album
import com.example.simplemediaapi.viewmodel.AlbumViewModel

class AlbumActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(AlbumViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      Получение данных от предыдущей активити
        viewModel.album = Album(
            intent.extras?.getInt(IntentConstants.idAlbum)!!,
            intent.extras?.getString(IntentConstants.artistName)!!,
            intent.extras?.getString(IntentConstants.albumName)!!,
            intent.extras?.getString(IntentConstants.albumImageUrl)!!
        )

        val binding: ActivityAlbumBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_album
        )

//      Отслеживание нажатия кнопки "Вернуться назад"
        viewModel.backPressed.observe(this, {
            onBackPressed()
        })

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }
}