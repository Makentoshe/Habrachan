package com.makentoshe.habrachan.di.main.articles

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesControllerViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class ArticlesControllerViewModelProvider(private val fragment: Fragment) : Provider<ArticlesControllerViewModel> {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): ArticlesControllerViewModel {
        val factory = ArticlesControllerViewModel.Factory(router)
        return ViewModelProviders.of(fragment, factory)[ArticlesControllerViewModel::class.java]
    }
}