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
import kotlinx.android.synthetic.main.activity_home.*


class AlbumFragment : Fragment() {

    private val viewModel by lazy {
        buildViewModel {
            AlbumViewModel(requireArguments().getParcelable("album")!!)}
    }
    private val networkViewModel by lazy { ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java) }
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAlbumBinding.inflate(inflater, container, false) // TODO перехожу с поиска, не подхватывается инфа о состоянии инета, выводится
            .apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkViewModel.networkConnectionChanges
            .take(1)
            .filter { isNetworkConnected -> !isNetworkConnected }
            .subscribe {
                activity?.toolbar?.title = getString(R.string.waitingNetworkConnection)
                // TODO message about no network connection
            }
            .addTo(disposables)

        networkViewModel.networkConnectionChanges
            .subscribe { isNetworkConnected ->
            viewModel.networkConnectionChanged(isNetworkConnected)
        }.addTo(disposables)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

}