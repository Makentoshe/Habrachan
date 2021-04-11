package com.makentoshe.habrachan.application.android.screen.user.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class UserScreen: SupportAppScreen() {

    override fun getFragment(): Fragment {
        return UserFragment.build()
    }
}