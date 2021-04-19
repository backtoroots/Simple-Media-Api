package com.example.simplemediaapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemediaapi.BR
import com.example.simplemediaapi.databinding.SongsListItemBinding
import com.example.simplemediaapi.viewmodel.AlbumViewModel

/**
 * Адаптер RecyclerView для песен.
 * @param layoutId ID шаблона - элемента RecyclerView
 * @param viewModel - объект ViewModel. Используется для реализации DataBinding
 */
class SongsRecyclerViewAdapter(
    private val layoutId: Int,
    private val viewModel: AlbumViewModel
) : RecyclerView.Adapter<SongsRecyclerViewAdapter.SongsViewHolder>() {

    class SongsViewHolder(private val binding: SongsListItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(viewModel: AlbumViewModel, position: Int?) {
            binding.setVariable(BR.viewmodel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<SongsListItemBinding>(inflater, viewType, parent, false)
        return SongsViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int = layoutId

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int = viewModel.displayedSongs.size
}