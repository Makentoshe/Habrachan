package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class GetCookieViewModelProvider @Inject constructor(
    private val factory: GetCookieViewModel.Factory,
) : ViewModelFragmentProvider<GetCookieViewModel> {
    override fun get(fragment: Fragment): GetCookieViewModel {
        return ViewModelProviders.of(fragment, factory)[GetCookieViewModel::class.java]
    }
}