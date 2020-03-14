package com.makentoshe.habrachan.di.article

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class UserAvatarViewModelProvider(private val fragment: Fragment): Provider<UserAvatarViewModel> {

    private val avatarDao by inject<AvatarDao>()
    private val avatarManager by inject<ImageManager>()
    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): UserAvatarViewModel {
        val application = fragment.requireActivity().application
        val factory = UserAvatarViewModel.Factory(avatarDao, application, avatarManager)
        return ViewModelProviders.of(fragment, factory)[UserAvatarViewModel::class.java]
    }
}
