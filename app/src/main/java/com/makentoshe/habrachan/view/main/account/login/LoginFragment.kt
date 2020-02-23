package com.makentoshe.habrachan.view.main.account.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.main.account.login.LoginData
import com.makentoshe.habrachan.ui.main.account.login.LoginFragmentUi
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class LoginFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val viewModel by inject<LoginViewModel>()
    private val navigator by inject<AccountFlowFragment.Navigator>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LoginFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailView = view.findViewById<TextInputEditText>(R.id.login_fragment_email_edittext)
        val passwordView = view.findViewById<TextInputEditText>(R.id.login_fragment_password_edittext)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.login_fragment_password)
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
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE).also {
                (it as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        viewModel.errorObservable.subscribe {
            passwordLayout.error = resources.getString(R.string.invalid_email_or_password)
        }.let(disposables::add)

        viewModel.loginObservable.subscribe {
            navigator.toUserScreen()
        }.let(disposables::add)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build() = LoginFragment()
    }
}
