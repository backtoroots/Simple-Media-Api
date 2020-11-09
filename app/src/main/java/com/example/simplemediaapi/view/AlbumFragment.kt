package com.example.simplemediaapi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplemediaapi.databinding.FragmentAlbumBinding
import com.example.simplemediaapi.viewmodel.AlbumViewModel


class AlbumFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(AlbumViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlbumBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.album = requireArguments().getParcelable("album")!!
    }


}