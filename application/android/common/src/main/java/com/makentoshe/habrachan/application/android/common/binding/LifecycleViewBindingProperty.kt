package com.makentoshe.habrachan.application.android.common.binding

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LifecycleViewBindingProperty<in LO : LifecycleOwner, out VB : ViewBinding>(
    private val factory: (LO) -> VB,
) : ReadOnlyProperty<LO, VB>, DefaultLifecycleObserver {

    private var viewBinding: VB? = null

    @MainThread
    override operator fun getValue(thisRef: LO, property: KProperty<*>): VB {
        if (thisRef.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            viewBinding = null
            return factory(thisRef)
        }

        return this.viewBinding ?: factory(thisRef).also { viewBinding ->
            this.viewBinding = viewBinding
        }
    }

    @MainThread
    override fun onDestroy(owner: LifecycleOwner) {
        if (Handler(Looper.getMainLooper()).post { internalOnDestroy() }) internalOnDestroy()
    }

    private fun internalOnDestroy() {
        viewBinding = null
    }
}

/** Attaches created ViewBinding to already created view */
inline fun <reified VB : ViewBinding> Fragment.attachBinding(): LifecycleViewBindingProperty<Fragment, VB> {
    val method = VB::class.java.getMethod("bind", View::class.java)
    return LifecycleViewBindingProperty { method.invoke(null, requireView()) as VB }
}

/** Attaches created ViewBinding to already created view */
inline fun <reified VB : ViewBinding> Fragment.viewBinding(): LifecycleViewBindingProperty<Fragment, VB> {
    val method = VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return LifecycleViewBindingProperty { method.invoke(null, layoutInflater, null, false) as VB }
}

inline fun <reified VB : ViewBinding> FragmentActivity.viewBinding(): LifecycleViewBindingProperty<Fragment, VB> {
    val method = VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return LifecycleViewBindingProperty { method.invoke(null, layoutInflater, null, false) as VB }
}
