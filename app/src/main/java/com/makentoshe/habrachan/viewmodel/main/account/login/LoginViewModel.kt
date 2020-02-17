package com.makentoshe.habrachan.viewmodel.main.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.model.main.account.login.LoginData
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class LoginViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    private val signInSubject = PublishSubject.create<LoginData>()
    val signInObserver: Observer<LoginData>
        get() = signInSubject

    init {
        signInSubject.subscribe {
            println(it)
        }.let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel() as T
        }
    }
}