package com.makentoshe.habrachan.model.main.account

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.account.AccountFragment

class AccountScreen : Screen() {
    override val fragment: Fragment
        get() = AccountFragment()
}