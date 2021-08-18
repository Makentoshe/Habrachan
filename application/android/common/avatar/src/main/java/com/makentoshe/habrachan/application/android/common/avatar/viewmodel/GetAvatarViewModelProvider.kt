package com.makentoshe.habrachan.application.android.common.avatar.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class GetAvatarViewModelProvider @Inject constructor(
    private val factory: GetAvatarViewModel.Factory,
): ViewModelFragmentProvider<GetAvatarViewModel> {
    override fun get(fragment: Fragment): GetAvatarViewModel {
        return ViewModelProviders.of(fragment, factory)[GetAvatarViewModel::class.java]
    }
}