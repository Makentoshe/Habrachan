package com.makentoshe.habrachan.application.android.screen.user.di.provider

import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import javax.inject.Inject
import javax.inject.Provider

class UserViewModelFactoryProvider: Provider<UserViewModel.Factory> {

    @Inject
    internal lateinit var androidUserSession: AndroidUserSession

    @Inject
    internal lateinit var getUserArena : GetUserArena

    @Inject
    internal lateinit var getAvatarArena : ContentArena

    override fun get(): UserViewModel.Factory {
        return UserViewModel.Factory(getUserArena, getAvatarArena, androidUserSession)
    }
}