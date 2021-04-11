package com.makentoshe.habrachan.application.android.screen.user.model

import com.makentoshe.habrachan.entity.User
import java.io.Serializable

sealed class UserAccount: Serializable {
    data class MeUserAccount(val user: User? = null): UserAccount(), Serializable
}