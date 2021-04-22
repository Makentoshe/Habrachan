package com.makentoshe.habrachan.application.android.screen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.broadcast.ApplicationStateBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.login.model.LoginSpec
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginNavigation
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.LoginViewModel
import com.makentoshe.habrachan.network.exception.LoginResponseException
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class LoginFragment : CoreFragment() {

    companion object {
        fun build() = LoginFragment()
    }

    override val arguments = Arguments(this)

    private val navigation by inject<LoginNavigation>()
    private val viewModel by inject<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener { /* workaround */ }
        fragment_login_toolbar.setNavigationOnClickListener {
            navigation.back()
        }

        fragment_login_email.editText?.addTextChangedListener {
            fragment_login_email.isErrorEnabled = false
        }

        fragment_login_password.editText?.addTextChangedListener {
            fragment_login_password.isErrorEnabled = false
        }

        fragment_login_button.setOnClickListener {
            closeSoftKeyboard()
            val email = fragment_login_email.editText?.text
            if (email.isNullOrBlank()) {
                fragment_login_email.error = getString(R.string.login_email_error_blank)
                return@setOnClickListener
            }
            val password = fragment_login_password.editText?.text
            if (password.isNullOrBlank()) {
                fragment_login_password.error = getString(R.string.login_password_error_blank)
                return@setOnClickListener
            }
            fragment_login_button.visibility = View.INVISIBLE
            fragment_login_progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                viewModel.loginChannel.send(LoginSpec(email.toString(), password.toString()))
            }
        }

        lifecycleScope.launch {
            viewModel.loginFlow.collectLatest { result ->
                result.fold({ response ->
                    ApplicationStateBroadcastReceiver.signIn(requireActivity())
                    navigation.toUserScreen(response.user)
                }, { throwable ->
                    if (throwable is LoginResponseException && throwable.other != null) {
                        fragment_login_password.error = getString(R.string.login_account_error)
                    } else {
                        fragment_login_password.error = getString(R.string.login_unknown_error)
                    }
                })
            }
        }
    }

    class Arguments(fragment: LoginFragment) : CoreFragment.Arguments(fragment)
}