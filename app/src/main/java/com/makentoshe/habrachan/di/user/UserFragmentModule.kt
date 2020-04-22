package com.makentoshe.habrachan.di.user

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.user.UserFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.user.UserViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class UserFragmentModule(fragment: UserFragment) : Module() {

    private val avatarManager: ImageManager
    private val usersManager: UsersManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val database by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        avatarManager = ImageManager.Builder(client).build()
        usersManager = UsersManager.Builder(client).build()

        bind<UserViewModel>().toInstance(getUserViewModel(fragment))
        bind<UserAvatarViewModel>().toInstance(getUserAvatarViewModel(fragment))
        bind<UserFragment.Navigator>().toInstance(UserFragment.Navigator(router))
    }

    private fun getUserAvatarViewModel(fragment: UserFragment): UserAvatarViewModel {
        val application = fragment.requireActivity().application
        val factory = UserAvatarViewModel.Factory(database.avatars(), application, avatarManager)
        return ViewModelProviders.of(fragment, factory)[UserAvatarViewModel::class.java]
    }

    private fun getUserViewModel(fragment: UserFragment): UserViewModel {
        val factory = UserViewModel.Factory(database, sessionDatabase, usersManager)
        return ViewModelProviders.of(fragment, factory)[UserViewModel::class.java]
    }
}
