package com.makentoshe.habrachan.application.android.screen.user.di

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.di.module.GetAvatarNetworkModule
import com.makentoshe.habrachan.application.android.screen.user.di.module.GetUserNetworkModule
import com.makentoshe.habrachan.application.android.screen.user.di.module.MeUserNetworkModule
import com.makentoshe.habrachan.application.android.screen.user.di.module.UserScreenModule
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class UserFragmentInjector : FragmentInjector<UserFragment>(UserFragment::class) {

    companion object : Analytics(LogAnalytic())

    override fun inject(injectorScope: FragmentInjectorScope<UserFragment>) {
        val scope = UserScope(injectorScope.fragment.arguments.login.getOrNull())

        val toothpickScope = commonUserScope(injectorScope).openSubScope(scope)
        val module = UserScreenModule(injectorScope)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.installModules(module).closeOnDestroy(injectorScope.fragment).inject(injectorScope.fragment)
    }

    private fun commonUserScope(injectorScope: FragmentInjectorScope<UserFragment>): Scope {
        val scope = UserScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val getUserNetworkModule = GetUserNetworkModule()
        captureModuleInstall(getUserNetworkModule, scope, injectorScope)
        val meUserNetworkModule = MeUserNetworkModule(injectorScope.context)
        captureModuleInstall(meUserNetworkModule, scope, injectorScope)
        val getAvatarNetworkModule = GetAvatarNetworkModule()
        captureModuleInstall(getAvatarNetworkModule, scope, injectorScope)
        return toothpickScope.installModules(getUserNetworkModule, meUserNetworkModule, getAvatarNetworkModule)
    }
}
