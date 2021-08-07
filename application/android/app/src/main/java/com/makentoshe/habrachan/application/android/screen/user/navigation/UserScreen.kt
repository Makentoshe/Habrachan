package com.makentoshe.habrachan.application.android.screen.user.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import ru.terrakok.cicerone.android.support.SupportAppScreen

class UserScreen(val account: UserAccount): SupportAppScreen() {

    override fun getFragment(): Fragment {
        return UserFragment.build(account)
    }
}

