package com.makentoshe.habrachan.di.post

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.viewmodel.post.PostFragmentNavigationViewModel
import ru.terrakok.cicerone.Router
import toothpick.ktp.delegate.inject
import javax.inject.Provider

/* Provides PostFragmentNavigationViewModel for PostFragment */
class PostFragmentNavigationViewModelProvider(
    private val fragment: Fragment
): Provider<PostFragmentNavigationViewModel> {

    private val router by inject<Router>()

    override fun get(): PostFragmentNavigationViewModel {
        val factory = createViewModelFactory()
        return ViewModelProviders.of(fragment, factory)[PostFragmentNavigationViewModel::class.java]
    }

    private fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        return PostFragmentNavigationViewModel.Factory(router)
    }
}