package com.makentoshe.habrachan.view.main.account.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.ui.main.account.login.LoginFragmentUi

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LoginFragmentUi().createView(requireContext())
    }

    class Factory {
        fun build() = LoginFragment()
    }
}
