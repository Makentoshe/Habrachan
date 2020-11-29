package com.makentoshe.habrachan.application.android

import android.os.Bundle

abstract class FragmentArguments<F: CoreFragment>(private val fragment: F) {
    
    init {
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    protected val fragmentArguments: Bundle
        get() = fragment.requireArguments()
}