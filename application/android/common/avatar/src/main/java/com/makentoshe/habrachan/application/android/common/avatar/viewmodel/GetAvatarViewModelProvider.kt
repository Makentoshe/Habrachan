package com.makentoshe.habrachan.application.android.common.avatar.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.common.arena.content.GetContentArena
import javax.inject.Inject

class GetAvatarViewModelProvider(
    private val factory: GetAvatarViewModel.Factory,
) : ViewModelFragmentProvider<GetAvatarViewModel> {

    @Inject constructor(
        stringsProvider: BundledStringsProvider,
        userSessionProvider: AndroidUserSessionProvider,
        getAvatarArena: GetContentArena,
    ) : this(GetAvatarViewModel.Factory(stringsProvider, userSessionProvider, getAvatarArena))

    override fun get(fragment: Fragment): GetAvatarViewModel {
        return ViewModelProviders.of(fragment, factory)[GetAvatarViewModel::class.java]
    }
}