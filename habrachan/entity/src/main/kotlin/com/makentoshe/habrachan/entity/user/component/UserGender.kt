package com.makentoshe.habrachan.entity.user.component

sealed class UserGender {
    abstract val int: Int
    abstract val string: String

    object Male: UserGender() {
        override val int = 1
        override val string = "Male"
    }

    object Female: UserGender() {
        override val int = 2
        override val string = "Female"
    }
}