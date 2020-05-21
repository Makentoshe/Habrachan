package com.makentoshe.habrachan.view

interface BackPressedFragment {
    /** Returns true if event was handled */
    fun onBackPressed(): Boolean
}