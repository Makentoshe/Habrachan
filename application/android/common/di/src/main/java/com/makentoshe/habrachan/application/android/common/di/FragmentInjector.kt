package com.makentoshe.habrachan.application.android.common.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import toothpick.config.Module
import kotlin.reflect.KClass

abstract class FragmentInjector<T : Fragment>(
    private val fragmentClass: KClass<T>,
    private val action: FragmentInjectorScope<T>.() -> Unit = {}
) : FragmentManager.FragmentLifecycleCallbacks() {

    companion object : Analytics(LogAnalytic()) {

        fun FragmentInjector<*>.captureModuleInstall(
            module: Module, scope: String, injectorScope: FragmentInjectorScope<*>
        ) = capture(analyticEvent {
            "Inject ${module::class.simpleName} in scope $scope to ${injectorScope.fragment}"
        })

        fun FragmentInjector<*>.captureModuleInstall(
            module: Module, scope: KClass<*>, injectorScope: FragmentInjectorScope<*>
        ) = captureModuleInstall(module, scope.simpleName.toString(), injectorScope)

        fun FragmentInjector<*>.captureModuleInstall(
            module: Module, scope: Any, injectorScope: FragmentInjectorScope<*>
        ) = captureModuleInstall(module, scope.toString(), injectorScope)

        fun FragmentInjector<*>.captureScopeOpened(
            scope: String, injectorScope: FragmentInjectorScope<*>
        ) = capture(analyticEvent{
            "Scope $scope already opened. Called for ${injectorScope.fragment}"
        })

        fun FragmentInjector<*>.captureScopeOpened(
            scope: Any, injectorScope: FragmentInjectorScope<*>
        ) = captureScopeOpened(scope.toString(), injectorScope)

        fun FragmentInjector<*>.captureScopeOpened(
            scope: KClass<*>, injectorScope: FragmentInjectorScope<*>
        ) = captureScopeOpened(scope.simpleName.toString(), injectorScope)
    }

    @Suppress("UNCHECKED_CAST") // We make a check before cast
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        if (f::class == fragmentClass) inject(FragmentInjectorScope(f as T, context, fm))
    }

    protected open fun inject(injectorScope: FragmentInjectorScope<T>) = action(injectorScope)

    data class FragmentInjectorScope<out T : Fragment>(
        val fragment: T, val context: Context, val manager: FragmentManager
    )
}
