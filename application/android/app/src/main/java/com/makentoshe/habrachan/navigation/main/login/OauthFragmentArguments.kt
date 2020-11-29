package com.makentoshe.habrachan.navigation.main.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.model.main.login.OauthType
import com.makentoshe.habrachan.view.main.login.OauthFragment

class OauthFragmentArguments(private val fragment: OauthFragment) {

    init {
        val fragment = fragment as Fragment
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
    }

    private val fragmentArguments: Bundle
        get() = fragment.requireArguments()

    var type: OauthType
        get() = OauthType.from(fragmentArguments.getString(TYPE, null))
        set(value) = fragmentArguments.putString(TYPE, value.socialType)

    companion object {
        private const val TYPE = "Type"
    }
}
