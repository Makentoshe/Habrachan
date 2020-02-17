package com.makentoshe.habrachan.view.main.account.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.main.account.login.LoginData
import com.makentoshe.habrachan.ui.main.account.login.LoginFragmentUi
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import toothpick.ktp.delegate.inject

class LoginFragment : Fragment() {

    private val viewModel by inject<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LoginFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailView = view.findViewById<TextView>(R.id.login_fragment_email)
        val passwordView = view.findViewById<TextView>(R.id.login_fragment_password)
        val signInView = view.findViewById<Button>(R.id.login_fragment_loginbutton)

        emailView.addTextChangedListener {
            signInView.isEnabled = !it.isNullOrEmpty() && !passwordView.text.isNullOrEmpty()
        }

        passwordView.addTextChangedListener {
            signInView.isEnabled = !it.isNullOrEmpty() && !passwordView.text.isNullOrEmpty()
        }

        signInView.setOnClickListener {
            val loginData = LoginData(emailView.text.toString(), passwordView.text.toString())
            viewModel.signInObserver.onNext(loginData)
        }
    }

    class Factory {
        fun build() = LoginFragment()
    }
}
