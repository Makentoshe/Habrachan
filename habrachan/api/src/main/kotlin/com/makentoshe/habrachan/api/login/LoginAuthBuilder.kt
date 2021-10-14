package com.makentoshe.habrachan.api.login

import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.delegate.RequireReadWriteProperty
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

class LoginAuthBuilderRequireReadWriteProperty<Type>(
    private val key: String,
    private val readMap: (String) -> Type,
    private val writeMap: (Type) -> String,
) : RequireReadWriteProperty<LoginAuthBuilder, Type>() {
    override fun getValue(thisRef: LoginAuthBuilder, property: KProperty<*>): Require<Type> {
        return Require(thisRef.mutableParameters[key]?.let(readMap))
    }

    override fun setValue(thisRef: LoginAuthBuilder, property: KProperty<*>, value: Require<Type>) {
        thisRef.mutableParameters[key] = value.value.let(writeMap)
    }
}

fun <Type> requireReadWriteProperty(
    key: String,
    readMap: (String) -> Type,
    writeMap: (Type) -> String,
) = LoginAuthBuilderRequireReadWriteProperty(key, readMap, writeMap)
