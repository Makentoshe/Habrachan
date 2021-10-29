package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class GetLoginViewModelProvider @Inject constructor(
    private val factory: GetLoginViewModel.Factory,
) : ViewModelFragmentProvider<GetLoginViewModel> {
    override fun get(fragment: Fragment): GetLoginViewModel {
        return ViewModelProviders.of(fragment, factory)[GetLoginViewModel::class.java]
    }
}