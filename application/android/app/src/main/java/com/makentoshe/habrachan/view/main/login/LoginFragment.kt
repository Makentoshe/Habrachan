package com.makentoshe.habrachan.view.main.login

import android.app.Activity
import android.content.Intent
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
import com.makentoshe.habrachan.application.android.core.ui.SnackbarErrorController
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import com.makentoshe.habrachan.model.main.login.LoginData
import com.makentoshe.habrachan.model.main.login.OauthType
import com.makentoshe.habrachan.navigation.main.login.OauthScreen
import com.makentoshe.habrachan.ui.main.account.login.LoginFragmentUi
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class LoginFragment : Fragment() {

    private val disposables by inject<CompositeDisposable>()
    private val loginViewModel by inject<LoginViewModel>()

    private lateinit var emailView: TextInputEditText
    private lateinit var passwordView: TextInputEditText
    private lateinit var signInView: Button
    private lateinit var oauthGithubSignIn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LoginFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        emailView = view.findViewById(R.id.login_fragment_email_edittext)
        passwordView = view.findViewById(R.id.login_fragment_password_edittext)
        signInView = view.findViewById(R.id.login_fragment_loginbutton)
        oauthGithubSignIn = view.findViewById(R.id.login_fragment_oauth_github)

        emailView.addTextChangedListener {
            signInView.isEnabled = !it.isNullOrEmpty() && !passwordView.text.isNullOrEmpty()
        }

        passwordView.addTextChangedListener {
            signInView.isEnabled = !it.isNullOrEmpty() && !emailView.text.isNullOrEmpty()
        }

        signInView.setOnClickListener {
            val loginData = LoginData.Default(emailView.text.toString(), passwordView.text.toString())
            loginViewModel.signInObserver.onNext(loginData)
            hideSoftKeyboard()
        }

        oauthGithubSignIn.setOnClickListener {
            val fragment = OauthScreen(OauthType.Github).fragment
            fragment.setTargetFragment(this, oauthRequestCode)
            parentFragmentManager.beginTransaction().add(R.id.login_flow_fragment, fragment).commit()
        }

        loginViewModel.loginObservable.observeOn(AndroidSchedulers.mainThread())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OAuthResponse::javaClass.hashCode()) {
            if (resultCode == Activity.RESULT_OK) {
                onOauthResultSuccess(data)
            } else {
                onOauthResultError(data)
            }
        }
    }

    private fun onOauthResultSuccess(data: Intent?) {
        if (data?.hasExtra("token") == true) {
            loginViewModel.signInObserver.onNext(LoginData.Token(data.getStringExtra("token")!!))
        } else {
            SnackbarErrorController.from(view ?: return).displayIndefiniteMessage("[hard]: token is null")
        }
    }

    private fun onOauthResultError(data: Intent?) {
        val message = data?.getStringExtra("ErrorMessage") ?: return
        SnackbarErrorController.from(view ?: return).displayIndefiniteMessage(message)
    }

    class Factory {
        fun build() = LoginFragment()
    }

    companion object {
        private const val oauthRequestCode = 1
    }
}
