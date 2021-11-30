package com.makentoshe.habrachan.application.android.screen.user

import com.makentoshe.habrachan.functional.Option2
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MeUserScreen : SupportAppScreen() {
    override fun getFragment() = UserFragment().apply {
        arguments.login = Option2.None
    }
}