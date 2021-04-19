package com.example.simplemediaapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemediaapi.BR
import com.example.simplemediaapi.databinding.AlbumsListItemBinding
import com.example.simplemediaapi.viewmodel.SearchViewModel

/**
 * Адаптер RecyclerView для альбомов.
 * @param layoutId ID шаблона - элемента RecyclerView
 * @param viewModel - объект ViewModel. Используется для реализации DataBinding
 */
class AlbumsRecyclerViewAdapter(
    private val layoutId: Int,
    private val viewModel: SearchViewModel
) : RecyclerView.Adapter<AlbumsRecyclerViewAdapter.AlbumsViewHolder>() {

    class AlbumsViewHolder(private val binding: AlbumsListItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(viewModel: SearchViewModel, position: Int?) {
            binding.setVariable(BR.viewmodel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<AlbumsListItemBinding>(inflater, viewType, parent, false)
        return AlbumsViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int = layoutId

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int = viewModel.displayedAlbums.size
}