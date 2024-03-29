package com.example.simplemediaapi.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.simplemediaapi.R
import com.example.simplemediaapi.constants.NavigateConstants
import com.example.simplemediaapi.databinding.FragmentSearchBinding
import com.example.simplemediaapi.utils.EventObserver
import com.example.simplemediaapi.utils.hideSoftKeyBoard
import com.example.simplemediaapi.viewmodel.NetworkViewModel
import com.example.simplemediaapi.viewmodel.SearchViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo


class SearchFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    private val networkViewModel by lazy { ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java) }
    private val disposables = CompositeDisposable()
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkViewModel.networkConnectionChanges.subscribe { isNetworkConnected ->
            viewModel.networkConnectionChanged(isNetworkConnected)
        }.addTo(disposables)

        binding.searchAlbumField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editTextField: Editable?) {
                viewModel.searchTextChanged(editTextField.toString())
            }
        })

        /**
         * Отслеживает событие клика по найденному альбому.
         * При возникновении события, стартует новую активити.
         */
        viewModel.toStart.observe(requireActivity(), EventObserver { album ->
            findNavController().navigate(
                R.id.action_searchFragment_to_albumFragment,
                bundleOf(NavigateConstants.album to album)
            )
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
        hideSoftKeyBoard(binding.searchAlbumField)
        _binding = null
    }
}