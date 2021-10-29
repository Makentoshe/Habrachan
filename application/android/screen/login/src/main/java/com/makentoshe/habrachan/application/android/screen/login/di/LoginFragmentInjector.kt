package com.makentoshe.habrachan.application.android.screen.login.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class LoginFragmentInjector : FragmentInjector<LoginFragment>(
    fragmentClass = LoginFragment::class,
) {
    override fun inject(injectorScope: FragmentInjectorScope<LoginFragment>) {
        val scope = LoginScope::class
        if (Toothpick.isScopeOpen(scope)) {
            return captureScopeOpened(scope, injectorScope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val module = LoginModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }
}