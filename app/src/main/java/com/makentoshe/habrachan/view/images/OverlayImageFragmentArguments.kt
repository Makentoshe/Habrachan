package com.makentoshe.habrachan.view.images

import android.os.Bundle
import androidx.fragment.app.Fragment

class OverlayImageFragmentArguments(private val fragment: OverlayImageFragment) {

    init {
        val fragment = fragment as Fragment
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    private val fragmentArguments: Bundle
        get() = fragment.requireArguments()

    var source: String
        get() = fragmentArguments.getString(SOURCE) ?: ""
        set(value) = fragmentArguments.putString(SOURCE, value)

    companion object {
        private const val SOURCE = "Source"
    }
}