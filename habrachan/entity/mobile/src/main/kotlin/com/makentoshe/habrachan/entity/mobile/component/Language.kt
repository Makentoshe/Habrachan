package com.makentoshe.habrachan.entity.mobile.component

sealed class Language {

    abstract val string: String

    object Russian : Language() {
        override val string = "ru"
    }

    object English : Language() {
        override val string = "en"
    }

    data class Custom(override val string: String) : Language()
}

fun String.toLanguage(): Language = when (this) {
    "en" -> Language.English
    "ru" -> Language.Russian
    else -> Language.Custom(this)
}