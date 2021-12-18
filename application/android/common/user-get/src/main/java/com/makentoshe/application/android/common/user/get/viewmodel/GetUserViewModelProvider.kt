package com.makentoshe.application.android.common.user.get.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class GetUserViewModelProvider @Inject constructor(
    private val factory: GetUserViewModel.Factory,
) : ViewModelFragmentProvider<GetUserViewModel> {

    override fun get(fragment: Fragment): GetUserViewModel {
        return ViewModelProviders.of(fragment, factory)[GetUserViewModel::class.java]
    }
}
