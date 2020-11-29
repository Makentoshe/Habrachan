package com.makentoshe.habrachan.di.main.login

import com.makentoshe.habrachan.view.main.login.LoginFragment
import io.reactivex.disposables.CompositeDisposable
import toothpick.config.Module
import toothpick.ktp.binding.bind

class LoginFragmentModule(fragment: LoginFragment) : Module() {

    init {
        val disposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(disposables)
    }
}