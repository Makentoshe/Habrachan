package com.makentoshe.habrachan.application.android.screen.comments.di.module

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.content.GetContentArena
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class GetAvatarViewModelModule(fragment: Fragment) : Module() {

    private val stringsProvider by inject<BundledStringsProvider>()
    private val userSessionProvider by inject<AndroidUserSessionProvider>()

    private val getAvatarArena by inject<GetContentArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val getAvatarViewModelFactory = GetAvatarViewModel.Factory(stringsProvider, userSessionProvider, getAvatarArena)
        val getAvatarViewModelProvider = GetAvatarViewModelProvider(getAvatarViewModelFactory)
        bind<GetAvatarViewModelProvider>().toInstance(getAvatarViewModelProvider)

        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))
    }
}