package com.makentoshe.habrachan.application.android.common.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class FragmentArguments(private val fragment: Fragment) {

    init {
        val fragment = fragment
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    protected val fragmentArguments: Bundle
        get() = fragment.requireArguments()
}