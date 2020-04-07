package com.makentoshe.habrachan.model.main.account.user

import java.io.Serializable

sealed class UserAccount : Serializable {
    object Me : UserAccount(), Serializable
    class User(val userName: String) : UserAccount(), Serializable
}