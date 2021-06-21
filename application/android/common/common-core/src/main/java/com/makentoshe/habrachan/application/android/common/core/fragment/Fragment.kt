package com.makentoshe.habrachan.application.android.common.core.fragment

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.closeSoftKeyboard() {
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = requireActivity().currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
