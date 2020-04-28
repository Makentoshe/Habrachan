package com.makentoshe.habrachan.view.main.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.model.main.login.LoginData
import com.makentoshe.habrachan.ui.main.account.login.LoginFragmentUi
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class LoginFragment : Fragment() {

    private val disposables by inject<CompositeDisposable>()
    private val viewModel by inject<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LoginFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailView = view.findViewById<TextInputEditText>(R.id.login_fragment_email_edittext)
        val passwordView = view.findViewById<TextInputEditText>(R.id.login_fragment_password_edittext)
        val signInView = view.findViewById<Button>(R.id.login_fragment_loginbutton)

        emailView.addTextChangedListener {
            signInView.isEnabled = !it.isNullOrEmpty() && !passwordView.text.isNullOrEmpty()
        }

        passwordView.addTextChangedListener {
            signInView.isEnabled = !it.isNullOrEmpty() && !emailView.text.isNullOrEmpty()
        }

        signInView.setOnClickListener {
            val loginData = LoginData(emailView.text.toString(), passwordView.text.toString())
            viewModel.signInObserver.onNext(loginData)
            hideSoftKeyboard()
        }

        viewModel.loginObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onLoginResponse)
            .let(disposables::add)

        if (BuildConfig.DEBUG) {
            emailView.setText(BuildConfig.LOGIN)
            passwordView.setText(BuildConfig.PASSWORD)
        }
    }

    private fun onLoginResponse(response: LoginResponse) = when (response) {
        is LoginResponse.Error -> onLoginResponseError(response)
        else -> Unit
    }

    private fun onLoginResponseError(response: LoginResponse.Error) {
        val view = view ?: return
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.login_fragment_password)
        passwordLayout.error = resources.getString(R.string.invalid_email_or_password)
    }

    private fun hideSoftKeyboard() {
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE).also {
            (it as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build() = LoginFragment()
    }
}
