package com.makentoshe.habrachan.api.login

import com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import com.makentoshe.habrachan.delegate.Require2ReadonlyProperty
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import kotlin.reflect.KProperty

/** Class which contains any parameters for performing a login action */
data class LoginAuth internal constructor(val parameters: Map<String, String>)


/** Property for delegation a specified property that optional for [LoginAuth] */
class LoginAuthOptionReadonlyProperty<Type>(
    private val key: String,
    private val map: (String) -> Type,
) : OptionReadonlyProperty<LoginAuth, Type>() {
    override fun getValue(thisRef: LoginAuth, property: KProperty<*>): Option2<Type> {
        return Option2.from(thisRef.parameters[key]?.let(map))
    }
}

/** Creates a [LoginAuthOptionReadonlyProperty] instance */
fun <Type> optionReadonlyProperty(key: String, map: (String) -> Type): LoginAuthOptionReadonlyProperty<Type> {
    return LoginAuthOptionReadonlyProperty(key, map)
}

/** Property for delegation a specified property that requireable for [LoginAuth], like password or login */
class LoginAuthRequire2ReadonlyProperty<Type>(
    private val key: String,
    private val map: (String) -> Type
) : Require2ReadonlyProperty<LoginAuth, Type>() {
    override fun getValue(thisRef: LoginAuth, property: KProperty<*>): Require2<Type> {
        return Require2(thisRef.parameters[key]?.let(map))
    }
}

/** Creates a [LoginAuthRequire2ReadonlyProperty] instance */
fun <Type> requireReadonlyProperty(key: String, map: (String) -> Type): LoginAuthRequire2ReadonlyProperty<Type> {
    return LoginAuthRequire2ReadonlyProperty(key, map)
}
