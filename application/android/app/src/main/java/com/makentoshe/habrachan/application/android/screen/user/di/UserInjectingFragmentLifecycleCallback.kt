package com.makentoshe.habrachan.application.android.screen.user.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class UserInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is UserFragment -> injectUserFragment(f)
        else -> Unit
    }

    private fun injectUserFragment(fragment: UserFragment) {
        val module = UserModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, UserScope::class)
        scope.installModules(module).closeOnDestroy(fragment).inject(fragment)
    }
}