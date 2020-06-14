package com.makentoshe.habrachan.navigation.comments

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class FragmentArguments<T: Fragment>(private val holderFragment: T) {

    init {
        val fragment = holderFragment as Fragment
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    protected val fragmentArguments: Bundle
        get() = holderFragment.requireArguments()
}