package com.makentoshe.habrachan.application.android.screen.content.di

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.content.ContentFragmentPage
import com.makentoshe.habrachan.application.android.screen.content.model.ContentActionBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.content.viewmodel.ContentViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedContentModule(injectorScope: FragmentInjector.FragmentInjectorScope<ContentFragmentPage>) : Module() {

    private val userSession by inject<UserSession>()
    private val getContentArena by inject<ContentArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ContentScope::class).inject(this)

        val contentActionBroadcastReceiver = ContentActionBroadcastReceiver(injectorScope.fragment.lifecycleScope)
        bind<ContentActionBroadcastReceiver>().toInstance(contentActionBroadcastReceiver)

        val factory = ContentViewModel.Factory(getContentArena, userSession)
        val viewModel = ViewModelProviders.of(injectorScope.fragment, factory)[ContentViewModel::class.java]
        bind<ContentViewModel>().toInstance(viewModel)
    }
}
