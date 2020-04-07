package com.makentoshe.habrachan.viewmodel.main.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.login.LoginResponse
import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.model.main.account.login.LoginData
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class LoginViewModel(
    private val sessionDao: SessionDao,
    private val loginManager: LoginManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val signInSubject = PublishSubject.create<LoginData>()
    val signInObserver: Observer<LoginData>
        get() = signInSubject

    private val loginSubject = BehaviorSubject.create<UserSession>()
    val loginObservable: Observable<UserSession>
        get() = loginSubject

    private val errorSubject = BehaviorSubject.create<LoginResponse.Error>()
    val errorObservable: Observable<LoginResponse.Error>
        get() = errorSubject

    init {
        signInSubject.observeOn(Schedulers.io()).subscribe {
            val session = sessionDao.get()!!
            val request = LoginRequest.Builder(session.clientKey, session.apiKey).build(it.email, it.password)
            loginManager.login(request).subscribe(::onLoginResponse).let(disposables::add)
        }.let(disposables::add)
    }

    private fun onLoginResponse(response: LoginResponse) = when(response) {
        is LoginResponse.Success -> onLoginSuccessResponse(response)
        is LoginResponse.Error -> onLoginErrorResponse(response)
    }

    private fun onLoginSuccessResponse(response: LoginResponse.Success) {
        val newSession = sessionDao.get()!!.copy(tokenKey = response.accessToken)
        loginSubject.onNext(newSession)
        sessionDao.insert(newSession)
    }

    private fun onLoginErrorResponse(response: LoginResponse.Error) {
        errorSubject.onNext(response)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val sessionDao: SessionDao,
        private val loginManager: LoginManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(sessionDao, loginManager) as T
        }
    }
}