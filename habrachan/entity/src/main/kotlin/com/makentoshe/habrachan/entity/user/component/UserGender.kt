package com.makentoshe.habrachan.entity.user.component

sealed class UserGender {
    abstract val int: Int
    abstract val string: String

    object Undefined: UserGender() {
        override val int = 0
        override val string = "Undefined"
    }

    object Male: UserGender() {
        override val int = 1
        override val string = "Male"
    }

    object Female: UserGender() {
        override val int = 2
        override val string = "Female"
    }

    companion object {
        fun valueOf(int: Int) = when(int) {
            1 -> Male
            2 -> Female
            else -> Undefined
        }
    }
}