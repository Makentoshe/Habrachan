package com.makentoshe.habrachan.application.android

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class CoreBottomSheetDialogFragment: BottomSheetDialogFragment() {

    abstract val arguments: Arguments

    abstract class Arguments(private val fragment: CoreBottomSheetDialogFragment) {

        init {
            val fragment = fragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        protected val fragmentArguments: Bundle
            get() = fragment.requireArguments()
    }

    protected fun closeSoftKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}