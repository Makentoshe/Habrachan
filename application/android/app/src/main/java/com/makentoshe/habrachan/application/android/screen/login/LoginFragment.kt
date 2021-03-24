package com.makentoshe.habrachan.application.android.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.login.model.LoginSpec
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        view.setOnClickListener { /* workaround */ }

        fragment_login_button.setOnClickListener {
            lifecycleScope.launch {
                val email = fragment_login_email.editText?.text
                val password = fragment_login_email.editText?.text
                viewModel.loginChannel.send(LoginSpec(email.toString(), password.toString()))
            }
        }

        lifecycleScope.launch {
            viewModel.loginFlow.collectLatest {
                println(it)
            }
        }
    }

    class Arguments(fragment: LoginFragment) : CoreFragment.Arguments(fragment)
}