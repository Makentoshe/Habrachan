package com.makentoshe.habrachan.application.android.common.di.provider

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * Provider for a ViewModel instance for a selected fragment.
 *
 * This provider intents to attach ViewModel to a different Fragment instances.
 */
interface ViewModelFragmentProvider<T: ViewModel> {
    fun get(fragment: Fragment): T
}
