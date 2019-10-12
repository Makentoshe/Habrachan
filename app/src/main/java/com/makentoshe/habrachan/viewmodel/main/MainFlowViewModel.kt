package com.makentoshe.habrachan.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class MainFlowViewModel(
    val router: Router,
    val navigatorHolder: NavigatorHolder,
    var page: Int
) : ViewModel() {



    class Factory(
        private val cicerone: Cicerone<Router>,
        private val page: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainFlowViewModel(
                cicerone.router,
                cicerone.navigatorHolder,
                page
            ) as T
        }
    }
}