package com.example.simplemediaapi.view

import android.os.Bundle
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
import com.example.simplemediaapi.viewmodel.SearchViewModel
import com.xyarim.users.utils.EventObserver


class SearchFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /**
         * Отслеживает событие клика по найденному альбому.
         * При возникновении события, стартует новую активити.
         */
        viewModel.toStart.observe(requireActivity(), EventObserver { value ->
            findNavController().navigate(
                R.id.action_searchFragment_to_albumFragment,
                bundleOf(NavigateConstants.album to value)
            )
        })

    }

}