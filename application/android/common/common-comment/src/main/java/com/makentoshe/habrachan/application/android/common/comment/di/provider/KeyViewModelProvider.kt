package com.makentoshe.habrachan.application.android.common.comment.di.provider

import androidx.lifecycle.ViewModel

/**
 * Provider for a ViewModel instance associated with a key.
 *
 * This provider intents to attach ViewModel to a common Fragment.
 */
interface ViewModelKeyProvider<VM: ViewModel> {
    fun get(key: String): VM
}