package com.makentoshe.habrachan.api.login

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.delegate.RequireReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import kotlin.reflect.KProperty

/** Class which contains any parameters for performing a login action */
data class LoginAuth internal constructor(val parameters: Map<String, String>)


/** Property for delegation a specified property that optional for [LoginAuth] */
class LoginAuthOptionReadonlyProperty<Type>(
    private val key: String,
    private val map: (String) -> Type,
) : OptionReadonlyProperty<LoginAuth, Type>() {
    override fun getValue(thisRef: LoginAuth, property: KProperty<*>): Option<Type> {
        return Option.from(thisRef.parameters[key]?.let(map))
    }
}

/** Creates a [LoginAuthOptionReadonlyProperty] instance */
fun <Type> optionReadonlyProperty(key: String, map: (String) -> Type): LoginAuthOptionReadonlyProperty<Type> {
    return LoginAuthOptionReadonlyProperty(key, map)
}

/** Property for delegation a specified property that requireable for [LoginAuth], like password or login */
class LoginAuthRequireReadonlyProperty<Type>(
    private val key: String,
    private val map: (String) -> Type
) : RequireReadonlyProperty<LoginAuth, Type>() {
    override fun getValue(thisRef: LoginAuth, property: KProperty<*>): Require<Type> {
        return Require(thisRef.parameters[key]?.let(map))
    }
}

/** Creates a [LoginAuthRequireReadonlyProperty] instance */
fun <Type> requireReadonlyProperty(key: String, map: (String) -> Type): LoginAuthRequireReadonlyProperty<Type> {
    return LoginAuthRequireReadonlyProperty(key, map)
}
