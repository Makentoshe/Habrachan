package com.makentoshe.habrachan.application.android.screen.login.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class LoginInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is LoginFragment -> injectLoginFragment(f)
        else -> Unit
    }

    private fun injectLoginFragment(fragment: LoginFragment) {
        val module = LoginModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, LoginScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}