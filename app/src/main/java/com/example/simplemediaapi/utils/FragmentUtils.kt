package com.example.simplemediaapi.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

fun Fragment.hideSoftKeyBoard() {
    val imm: InputMethodManager =
        activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity?.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun disableContentInteraction(editText: TextInputEditText) {
    editText.keyListener = null
    editText.isFocusable = false
    editText.isFocusableInTouchMode = false
    editText.clearFocus()
}
