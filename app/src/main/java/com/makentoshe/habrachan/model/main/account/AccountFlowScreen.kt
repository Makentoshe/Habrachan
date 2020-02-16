package com.makentoshe.habrachan.model.main.account

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment

class AccountFlowScreen : Screen() {
    override val fragment: Fragment
        get() = AccountFlowFragment.Factory().build()
}