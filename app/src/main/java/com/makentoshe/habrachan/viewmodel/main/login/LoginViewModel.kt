package com.makentoshe.habrachan.viewmodel.main.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.OAuthRequest
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import com.makentoshe.habrachan.model.main.login.LoginData
import com.makentoshe.habrachan.model.main.login.OauthType
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class LoginViewModel(
    private val sessionDatabase: SessionDatabase,
    private val loginManager: LoginManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val signInSubject = PublishSubject.create<LoginData>()
    val signInObserver: Observer<LoginData>
        get() = signInSubject

    private val loginSubject = BehaviorSubject.create<LoginResponse>()
    val loginObservable: Observable<LoginResponse>
        get() = loginSubject

    private val oauthRequestSubject = PublishSubject.create<OauthType>()
    val oauthRequestObserver: Observer<OauthType> = oauthRequestSubject

    private val oauthResponseSubject = BehaviorSubject.create<OAuthResponse>()
    val oauthResponseObservable: Observable<OAuthResponse> = oauthResponseSubject

    init {
        signInSubject.observeOn(Schedulers.io()).subscribe(::onSignIn).let(disposables::add)

        oauthRequestSubject.observeOn(Schedulers.io()).map {
            val session = sessionDatabase.session().get()
            val request = OAuthRequest(session.clientKey, it.socialType, it.hostUrl)
            return@map loginManager.oauth(request).blockingGet()
        }.subscribe({ response ->
            oauthResponseSubject.onNext(response)
        }, { throwable ->
            oauthResponseSubject.onNext(OAuthResponse.Error(throwable.toString()))
        }).let(disposables::add)
    }

    private fun onSignIn(loginData: LoginData) {
        when (loginData) {
            is LoginData.Default -> defaultSignIn(loginData)
            is LoginData.Token -> tokenSignIn(loginData)
        }
    }

    private fun defaultSignIn(loginData: LoginData.Default) {
        val session = sessionDatabase.session().get()
        val request = LoginRequest.Builder(session.clientKey, session.apiKey).build(loginData.email, loginData.password)
        loginManager.login(request)
            .doOnSuccess(::onLoginResponse)
            .subscribe(loginSubject::onNext)
            .let(disposables::add)
    }

    private fun tokenSignIn(loginData: LoginData.Token) {
        val loginResponse = LoginResponse.Success(loginData.token, "")
        onLoginResponse(loginResponse)
        loginSubject.onNext(loginResponse)
    }

    private fun onLoginResponse(response: LoginResponse) = when (response) {
        is LoginResponse.Success -> onLoginSuccessResponse(response)
        is LoginResponse.Error -> Unit
    }

    private fun onLoginSuccessResponse(response: LoginResponse.Success) {
        val currentSession = sessionDatabase.session().get()
        val newSession = currentSession.copy(tokenKey = response.accessToken)
        sessionDatabase.session().insert(newSession)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val sessionDatabase: SessionDatabase,
        private val loginManager: LoginManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(sessionDatabase, loginManager) as T
        }
    }
}