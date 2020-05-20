package com.makentoshe.habrachan.view

/** Allows to handle on back press event in fragment */
interface OnBackPressedFragment {

    /** Returns true if back press was handled and false otherwise */
    fun onBackPressed(): Boolean
}