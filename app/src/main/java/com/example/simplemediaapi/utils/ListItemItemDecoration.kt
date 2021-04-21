package com.example.simplemediaapi.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListItemItemDecoration(private val sizeOfSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition != 0) {
            outRect.top = sizeOfSpacing
        }
        outRect.left = sizeOfSpacing
        outRect.right = sizeOfSpacing
    }
}