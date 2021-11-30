package com.makentoshe.habrachan.application.android.screen.user.di.module

import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModel
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModelRequest
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.functional.Option2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class CommonUserModule : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
        bind<Option2<MeUserViewModelRequest>>().toInstance(Option2.from(MeUserViewModelRequest()))

        bind<MeUserViewModel.Factory>().toClass<MeUserViewModel.Factory>().singleton()
    }
}