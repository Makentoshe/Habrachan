package com.makentoshe.habrachan.application.android.screen.content.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.content.ContentFragment
import com.makentoshe.habrachan.application.android.screen.content.ContentFragmentPage
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ContentInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ContentFragmentPage -> injectContentFragmentPage(f)
        is ContentFragment -> injectContentFragment(f)
        else -> Unit
    }

    private fun injectContentFragment(fragment: ContentFragment) {
        val module = ContentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ContentScope::class)
        scope.installModules(module).closeOnDestroy(fragment).inject(fragment)
    }

    private fun injectContentFragmentPage(fragment: ContentFragmentPage) {
        val module = ContentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ContentScope::class, fragment)
        scope.installModules(module).closeOnDestroy(fragment).inject(fragment)
    }
}