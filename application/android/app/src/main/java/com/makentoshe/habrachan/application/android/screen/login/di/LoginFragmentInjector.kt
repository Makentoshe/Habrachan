package com.makentoshe.habrachan.application.android.screen.login.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class LoginFragmentInjector: FragmentInjector<LoginFragment>(
    fragmentClass = LoginFragment::class,
    action = {
        val scope = Toothpick.openScopes(ApplicationScope::class, LoginScope::class)
        scope.installModules(LoginModule(fragment)).closeOnDestroy(fragment).inject(fragment)
    }
)