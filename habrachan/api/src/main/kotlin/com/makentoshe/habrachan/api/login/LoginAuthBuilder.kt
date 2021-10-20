package com.makentoshe.habrachan.api.login

import com.makentoshe.habrachan.delegate.Require2ReadWriteProperty
import com.makentoshe.habrachan.functional.Require2
import kotlin.reflect.KProperty

class LoginAuthBuilder(
    val mutableParameters: MutableMap<String, String> = mutableMapOf(),
    factory: LoginAuthBuilder.() -> Unit = {}
) {

    init {
        this.factory()
    }

    fun build(factory: LoginAuthBuilder.() -> Unit = {}): LoginAuth {
        return factory.invoke(this).run { LoginAuth(mutableParameters) }
    }
}

class LoginAuthBuilderRequire2ReadWriteProperty<Type>(
    private val key: String,
    private val readMap: (String) -> Type,
    private val writeMap: (Type) -> String,
) : Require2ReadWriteProperty<LoginAuthBuilder, Type>() {
    override fun getValue(thisRef: LoginAuthBuilder, property: KProperty<*>): Require2<Type> {
        return Require2(thisRef.mutableParameters[key]?.let(readMap))
    }

    override fun setValue(thisRef: LoginAuthBuilder, property: KProperty<*>, value: Require2<Type>) {
        thisRef.mutableParameters[key] = value.value.let(writeMap)
    }
}

fun <Type> requireReadWriteProperty(
    key: String,
    readMap: (String) -> Type,
    writeMap: (Type) -> String,
) = LoginAuthBuilderRequire2ReadWriteProperty(key, readMap, writeMap)
