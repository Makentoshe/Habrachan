package com.makentoshe.habrachan.application.android.screen.login.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.MobileLoginFragment
import com.makentoshe.habrachan.application.android.screen.login.NativeLoginFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class LoginInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is NativeLoginFragment -> injectNativeLoginFragment(f)
        is MobileLoginFragment -> injectMobileLoginFragment(f)
        else -> Unit
    }

    private fun injectNativeLoginFragment(fragment: NativeLoginFragment) {
        val module = NativeLoginModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, LoginScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectMobileLoginFragment(fragment: MobileLoginFragment) {
        val module = MobileLoginModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, LoginScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}