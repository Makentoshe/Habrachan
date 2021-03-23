package com.makentoshe.habrachan.application.android.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.LoginViewModel
import toothpick.ktp.delegate.inject

class LoginFragment : CoreFragment() {

    companion object {
        fun build() = LoginFragment()
    }

    override val arguments = Arguments(this)

    private val viewModel by inject<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener {  }
    }

    class Arguments(fragment: LoginFragment) : CoreFragment.Arguments(fragment)
}