package com.makentoshe.habrachan.application.android

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class CoreFragment : Fragment() {

    abstract val arguments: Arguments

    abstract class Arguments(private val fragment: CoreFragment) {

        init {
            val fragment = fragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        protected val fragmentArguments: Bundle
            get() = fragment.requireArguments()
    }
}