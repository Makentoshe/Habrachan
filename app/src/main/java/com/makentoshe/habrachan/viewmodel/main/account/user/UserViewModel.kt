package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserViewModel : ViewModel() {

    

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel() as T
        }
    }
}