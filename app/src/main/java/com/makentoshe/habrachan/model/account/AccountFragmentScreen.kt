package com.makentoshe.habrachan.model.account

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.model.navigation.Screen
import com.makentoshe.habrachan.view.account.AccountFragment

class AccountScreen : Screen() {
    override val fragment: Fragment
        get() = AccountFragment()
}