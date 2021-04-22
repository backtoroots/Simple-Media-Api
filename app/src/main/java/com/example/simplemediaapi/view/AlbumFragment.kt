package com.example.simplemediaapi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.simplemediaapi.R
import com.example.simplemediaapi.databinding.FragmentAlbumBinding
import com.example.simplemediaapi.utils.buildViewModel
import com.example.simplemediaapi.viewmodel.AlbumViewModel
import com.example.simplemediaapi.viewmodel.NetworkViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo


class AlbumFragment : Fragment() {

    private val viewModel by lazy {
        buildViewModel {
            AlbumViewModel(requireArguments().getParcelable("album")!!)}
    }
    private val networkViewModel by lazy { ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java) }
    private val disposables = CompositeDisposable()
    private var _binding: FragmentAlbumBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkViewModel.networkConnectionChanges
            .subscribe { isNetworkConnected ->
            viewModel.networkConnectionChanged(isNetworkConnected)
        }.addTo(disposables)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
        _binding = null
    }

}