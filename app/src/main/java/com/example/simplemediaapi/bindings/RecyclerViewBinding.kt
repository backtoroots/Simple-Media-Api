package com.example.simplemediaapi.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemediaapi.utils.ListItemItemDecoration
import com.squareup.picasso.Picasso

class RecyclerViewBinding {

    companion object {

        /**
         * BindingAdapter для привязки layoutManager и адаптера к RecyclerView.
         */
        @JvmStatic
        @BindingAdapter("app:setAdapter")
        fun bindRecyclerViewAdapter(recyclerView: RecyclerView,
                                    adapter: RecyclerView.Adapter<*>?) {
                recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.addItemDecoration(ListItemItemDecoration(10))
                recyclerView.adapter = adapter
        }

        /**
         * BindingAdapter осуществляющий загрузку фотографий.
         */
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun bindRecyclerViewAdapter(imageView: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(imageView) // TODO add photo by default before correct connected
            }
        }

    }
}