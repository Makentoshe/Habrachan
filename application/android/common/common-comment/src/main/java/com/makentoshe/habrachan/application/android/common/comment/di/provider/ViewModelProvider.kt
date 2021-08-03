package com.makentoshe.habrachan.application.android.common.comment.di.provider

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

// TODO move to common-di module
interface ViewModelProvider<T: ViewModel> {
    fun get(fragment: Fragment): T
}
