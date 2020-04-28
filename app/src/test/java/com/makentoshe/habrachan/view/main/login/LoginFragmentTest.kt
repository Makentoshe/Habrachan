package com.makentoshe.habrachan.view.main.login

import android.content.Intent
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.login.LoginFlowFragmentScope
import com.makentoshe.habrachan.di.main.login.LoginFragmentScope
import com.makentoshe.habrachan.model.main.login.LoginData
import com.makentoshe.habrachan.model.main.login.LoginScreen
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class LoginFragmentTest : BaseRobolectricTest() {

    private val router by inject<Router>()
    private val mockViewModel = mockk<LoginViewModel>(relaxed = true)
    private val disposables = spyk(CompositeDisposable())

    init {
        mockBuildConfigField("DEBUG", false)
    }

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java,
            LoginFlowFragmentScope::class.java,
            LoginFragmentScope::class.java
        ).installTestModules(module {
            bind<LoginViewModel>().toInstance(mockViewModel)
            bind<CompositeDisposable>().toInstance(disposables)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(LoginFragmentScope::class.java)
    }

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    @Test
    fun testShouldCheckLoginFragmentUiAtStartup() {
        val loginObservable = PublishSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val emailInputLayout = activity.findViewById<TextInputLayout>(R.id.login_fragment_email)
        // email input layout should have properly hint
        assertEquals(emailInputLayout.hint, activity.getString(R.string.email))

        val emailEditText = activity.findViewById<EditText>(R.id.login_fragment_email_edittext)
        // email edit text input type should be textEmailAddress
        assert((emailEditText.inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)

        val passwordEditText = activity.findViewById<EditText>(R.id.login_fragment_password_edittext)
        // password edit text input type should be textPassword
        assert((passwordEditText.inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD)

        val passwordInputLayout = activity.findViewById<TextInputLayout>(R.id.login_fragment_password)
        // password input layout should have properly hint
        assertEquals(passwordInputLayout.hint, activity.getString(R.string.password))

        val signInButton = activity.findViewById<Button>(R.id.login_fragment_loginbutton)
        // button should have properly text
        assertEquals(signInButton.text, activity.getString(R.string.sign_in))

        val toolbar = activity.findViewById<Toolbar>(R.id.login_fragment_toolbar)
        // toolbar should have properly title
        assertEquals(toolbar.title, activity.getString(R.string.Authorization))
    }

    @Test
    fun testShouldCheckErrorMessageOnLoginRequestError() {
        val loginObservable = PublishSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val errorLoginResponse = mockk<LoginResponse.Error>()
        loginObservable.onNext(errorLoginResponse)

        val passwordInputLayout = activity.findViewById<TextInputLayout>(R.id.login_fragment_password)
        // password input layout should have properly error
        assertEquals(passwordInputLayout.error, activity.getString(R.string.invalid_email_or_password))
    }

    @Test
    fun testShouldCheckSignInButtonEnableStateOnStartUp() {
        val loginObservable = PublishSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val emailEditText = activity.findViewById<EditText>(R.id.login_fragment_email_edittext)
        // email edit text should be empty
        assert(emailEditText.text.isEmpty())

        val passwordEditText = activity.findViewById<EditText>(R.id.login_fragment_password_edittext)
        // password edit text should be empty
        assert(passwordEditText.text.isEmpty())

        val signInButton = activity.findViewById<Button>(R.id.login_fragment_loginbutton)
        // signInButton should be disabled for empty email and password
        assert(!signInButton.isEnabled)
    }

    @Test
    fun testShouldCheckSignInButtonEnableStateForEmptyEmailField() {
        val loginObservable = PublishSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val emailEditText = activity.findViewById<EditText>(R.id.login_fragment_email_edittext)
        emailEditText.setText("")
        Shadows.shadowOf(emailEditText).watchers.forEach { it.afterTextChanged(emailEditText.text) }

        val passwordEditText = activity.findViewById<EditText>(R.id.login_fragment_password_edittext)
        passwordEditText.setText("password")
        Shadows.shadowOf(passwordEditText).watchers.forEach { it.afterTextChanged(passwordEditText.text) }

        val signInButton = activity.findViewById<Button>(R.id.login_fragment_loginbutton)
        // signInButton should be disabled for empty email
        assert(!signInButton.isEnabled)
    }

    @Test
    fun testShouldCheckSignInButtonEnableStateForEmptyPasswordField() {
        val loginObservable = PublishSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val emailEditText = activity.findViewById<EditText>(R.id.login_fragment_email_edittext)
        emailEditText.setText("email")
        Shadows.shadowOf(emailEditText).watchers.forEach { it.afterTextChanged(emailEditText.text) }

        val passwordEditText = activity.findViewById<EditText>(R.id.login_fragment_password_edittext)
        passwordEditText.setText("")
        Shadows.shadowOf(passwordEditText).watchers.forEach { it.afterTextChanged(passwordEditText.text) }

        val signInButton = activity.findViewById<Button>(R.id.login_fragment_loginbutton)
        // signInButton should be disabled for empty email
        assert(!signInButton.isEnabled)
    }

    @Test
    fun testShouldCheckSignInButtonEnableStateForFilledPasswordAndEmailFields() {
        val loginObservable = PublishSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val emailEditText = activity.findViewById<EditText>(R.id.login_fragment_email_edittext)
        emailEditText.setText("email")
        Shadows.shadowOf(emailEditText).watchers.forEach { it.afterTextChanged(emailEditText.text) }

        val passwordEditText = activity.findViewById<EditText>(R.id.login_fragment_password_edittext)
        passwordEditText.setText("password")
        Shadows.shadowOf(passwordEditText).watchers.forEach { it.afterTextChanged(passwordEditText.text) }

        val signInButton = activity.findViewById<Button>(R.id.login_fragment_loginbutton)
        // signInButton should be enabled for both filled fields
        assert(signInButton.isEnabled)
    }

    @Test
    fun testShouldSendLoginDataToViewModelOnSignInButtonClick() {
        val signInObserver = BehaviorSubject.create<LoginData>()
        every { mockViewModel.signInObserver } returns signInObserver

        val activity = activityController.setup().get()
        router.navigateTo(LoginScreen())

        val emailEditText = activity.findViewById<EditText>(R.id.login_fragment_email_edittext)
        emailEditText.setText("email")
        Shadows.shadowOf(emailEditText).watchers.forEach { it.afterTextChanged(emailEditText.text) }

        val passwordEditText = activity.findViewById<EditText>(R.id.login_fragment_password_edittext)
        passwordEditText.setText("password")
        Shadows.shadowOf(passwordEditText).watchers.forEach { it.afterTextChanged(passwordEditText.text) }

        val signInButton = activity.findViewById<Button>(R.id.login_fragment_loginbutton)
        signInButton.performClick()

        signInObserver.test().assertValue { loginData ->
            loginData.password == passwordEditText.text.toString()
        }.assertValue { loginData ->
            loginData.email == emailEditText.text.toString()
        }
    }

    @Test
    fun testShouldCheckDisposablesClearedOnFragmentDestroy() {
        activityController.setup().get()
        router.navigateTo(LoginScreen())
        router.exit()

        verify { disposables.clear() }
    }


}