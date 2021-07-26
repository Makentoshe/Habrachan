package com.makentoshe.habrachan.application.android.screen.main.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.main.MainFlowFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class MainFlowFragmentInjector : FragmentInjector<MainFlowFragment>(fragmentClass = MainFlowFragment::class, action = {
    val toothpickModule = MainFlowModule(fragment)
    val toothpickScope = Toothpick.openScopes(ApplicationScope::class, MainFlowScope::class)
    toothpickScope.installModules(toothpickModule).closeOnDestroy(fragment).inject(fragment)
})
