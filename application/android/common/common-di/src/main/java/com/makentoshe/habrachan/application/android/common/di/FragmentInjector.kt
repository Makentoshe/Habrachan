package com.makentoshe.habrachan.application.android.common.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

abstract class FragmentInjector<T : Fragment>(
    private val fragmentClass: KClass<T>,
    private val action: FragmentInjectorScope<T>.() -> Unit = {}
) : FragmentManager.FragmentLifecycleCallbacks() {

    @Suppress("UNCHECKED_CAST") // We make a check before cast
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        if (f::class == fragmentClass) inject(FragmentInjectorScope(f as T, context, fm))
    }

    protected open fun inject(injectorScope: FragmentInjectorScope<T>) = action(injectorScope)

    data class FragmentInjectorScope<out T : Fragment>(
        val fragment: T, val context: Context, val manager: FragmentManager
    )
}
