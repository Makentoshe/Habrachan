package com.makentoshe.habrachan.navigation.main.login

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.model.main.login.OauthType
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.main.login.OauthFragment

class OauthScreen(private val type: OauthType) : Screen() {
    override fun getFragment(): Fragment {
        val fragment = OauthFragment()
        val arguments = OauthFragmentArguments(fragment)
        arguments.type = type
        return fragment
    }
}