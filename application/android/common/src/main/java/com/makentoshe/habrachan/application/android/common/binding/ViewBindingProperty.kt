package com.makentoshe.habrachan.application.android.common.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingProperty<out VB : ViewBinding>(private val factory: () -> VB) : ReadOnlyProperty<Any, VB> {

    private var viewBinding: VB? = null

    override operator fun getValue(thisRef: Any, property: KProperty<*>): VB {
        return this.viewBinding ?: factory().also { viewBinding ->
            this.viewBinding = viewBinding
        }
    }
}

inline fun <reified VB : ViewBinding> ViewGroup.viewBinding(): ViewBindingProperty<VB> {
    val method = VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return ViewBindingProperty { method.invoke(null, LayoutInflater.from(context), null, false) as VB }
}
