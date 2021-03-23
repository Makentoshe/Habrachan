package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModel() : ViewModel() {



    class Factory() : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = LoginViewModel() as T

    }
}