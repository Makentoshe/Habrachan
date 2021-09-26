package com.makentoshe.habrachan.application.android.screen.user.di

import com.makentoshe.habrachan.application.android.arena.ContentArenaCache
import com.makentoshe.habrachan.application.android.arena.GetUserArenaCache
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.di.provider.UserViewModelFactoryProvider
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserNavigation
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.manager.GetUserManager
import com.makentoshe.habrachan.network.request.GetUserRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommonUserModule(
    injectorScope: FragmentInjector.FragmentInjectorScope<UserFragment>,
) : Module() {

    private val androidCacheDatabase by inject<AndroidCacheDatabase>()
    private val getUserManager by inject<GetUserManager<GetUserRequest>>()
    private val getContentManager by inject<GetContentManager>()

    private val stackRouter by inject<StackRouter>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        bind<UserNavigation>().toInstance(UserNavigation(stackRouter))
        bind<GetUserArena>().toInstance(GetUserArena(getUserManager, GetUserArenaCache(androidCacheDatabase)))

        val contentArenaCache = ContentArenaCache(androidCacheDatabase.contentDao(), injectorScope.context.cacheDir)
        bind<ContentArena>().toInstance(ContentArena(getContentManager, contentArenaCache))

        bind<UserViewModel.Factory>().toProvider(UserViewModelFactoryProvider::class).providesSingleton()
    }
}