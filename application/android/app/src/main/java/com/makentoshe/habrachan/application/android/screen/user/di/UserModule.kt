package com.makentoshe.habrachan.application.android.screen.user.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.arena.ContentArenaCache
import com.makentoshe.habrachan.application.android.arena.GetUserArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.manager.GetUserManager
import com.makentoshe.habrachan.network.request.GetUserRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class UserScope

class UserModule(fragment: UserFragment): Module() {

    private val androidUserSession by inject<AndroidUserSession>()
    private val androidCacheDatabase by inject<AndroidCacheDatabase>()
    private val getUserManager by inject<GetUserManager<GetUserRequest>>()
    private val getContentManager by inject<GetContentManager>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)

        val contentArenaCache = ContentArenaCache(androidCacheDatabase.contentDao(), fragment.requireContext().cacheDir)
        val getContentArena = ContentArena(getContentManager, contentArenaCache)

        val getUserArena = GetUserArena(getUserManager, GetUserArenaCache(androidCacheDatabase))

        val viewModelFactory = UserViewModel.Factory(getUserArena, getContentArena, androidUserSession)
        val viewModel = ViewModelProviders.of(fragment, viewModelFactory)[UserViewModel::class.java]
        bind<UserViewModel>().toInstance(viewModel)
    }
}
