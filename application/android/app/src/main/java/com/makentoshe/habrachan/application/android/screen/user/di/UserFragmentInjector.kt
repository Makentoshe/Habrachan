package com.makentoshe.habrachan.application.android.screen.user.di

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class UserFragmentInjector : FragmentInjector<UserFragment>(UserFragment::class) {

    companion object : Analytics(LogAnalytic())

    override fun inject(injectorScope: FragmentInjectorScope<UserFragment>) {
        val scope = UserScope(injectorScope.fragment.arguments.account)
        val toothpickScope =  commonUserScope(injectorScope).openSubScope(scope)
        val module = SpecificUserModule(injectorScope)
        toothpickScope.installModules(module).closeOnDestroy(injectorScope.fragment).inject(injectorScope.fragment)

        capture(analyticEvent {
            "Inject ${module::class.simpleName} in scope $scope to ${injectorScope.fragment}"
        })
    }

    private fun commonUserScope(injectorScope: FragmentInjectorScope<UserFragment>): Scope {
        if (Toothpick.isScopeOpen(ArticleScope::class)) {
            return Toothpick.openScopes(ApplicationScope::class, UserScope::class)
        }

        val module = CommonUserModule(injectorScope)
        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, UserScope::class).installModules(module)

        capture(analyticEvent {
            "Inject ${module::class.simpleName} in scope ${UserScope::class.simpleName} to ${injectorScope.fragment}"
        })

        return toothpickScope
    }
}
