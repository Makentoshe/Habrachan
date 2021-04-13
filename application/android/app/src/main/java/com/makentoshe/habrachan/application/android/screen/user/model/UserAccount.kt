package com.makentoshe.habrachan.application.android.screen.user.model

import java.io.Serializable

sealed class UserAccount: Serializable {
    data class Me(val login: String? = null): UserAccount(), Serializable
}