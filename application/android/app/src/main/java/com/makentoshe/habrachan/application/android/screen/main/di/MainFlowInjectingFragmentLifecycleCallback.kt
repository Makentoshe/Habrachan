package com.makentoshe.habrachan.application.android.screen.main.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.screen.main.MainFlowFragment
import com.makentoshe.habrachan.di.common.ApplicationScope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class MainFlowInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is MainFlowFragment -> injectMainFlowFragment(f)
        else -> Unit
    }

    private fun injectMainFlowFragment(fragment: MainFlowFragment) {
        val module = MainFlowModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, MainFlowScope::class)
        scope.installModules(module).closeOnDestroy(fragment).inject(fragment)
    }

}
