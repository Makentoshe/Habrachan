package com.makentoshe.habrachan.application.android.common.strings

interface StringsProvider<T> {

    fun getString(key: T): String

}

