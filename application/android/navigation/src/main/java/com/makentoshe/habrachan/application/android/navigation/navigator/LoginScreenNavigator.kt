package com.makentoshe.habrachan.application.android.navigation.navigator

interface LoginScreenNavigator {
    fun toLoginScreen(shouldNavigateToUserScreenAfterLogin: Boolean = false)
}