package com.makentoshe.habrachan.application.android.common.user.me.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class MeUserViewModelProvider @Inject constructor(
    private val factory: MeUserViewModel.Factory,
) : ViewModelFragmentProvider<MeUserViewModel> {

    override fun get(fragment: Fragment): MeUserViewModel {
        return ViewModelProviders.of(fragment, factory)[MeUserViewModel::class.java]
    }
}
