package com.makentoshe.habrachan.application.android.screen.image.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.image.OverlayImageFragment
import com.makentoshe.habrachan.application.android.screen.image.navigation.OverlayImageFragmentNavigation
import com.makentoshe.habrachan.application.android.screen.image.viewmodel.OverlayImageFragmentViewModel
import com.makentoshe.habrachan.common.network.manager.ImageManager
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class OverlayImageFragmentScope

class OverlayImageFragmentModule(fragment: OverlayImageFragment) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val fragmentDisposables = CompositeDisposable()
        bind<CompositeDisposable>().toInstance(fragmentDisposables)

        val navigator = OverlayImageFragmentNavigation(router)
        bind<OverlayImageFragmentNavigation>().toInstance(navigator)

        val viewModelDisposables = CompositeDisposable()
        val postImageFragmentViewModel = getPostImageFragmentViewModel(fragment, viewModelDisposables)
        bind<OverlayImageFragmentViewModel>().toInstance(postImageFragmentViewModel)
    }

    private fun getPostImageFragmentViewModel(
        fragment: OverlayImageFragment, disposables: CompositeDisposable
    ) : OverlayImageFragmentViewModel {
        val imageManager = ImageManager.Builder(client).build()
        val factory = OverlayImageFragmentViewModel.Factory(fragment.arguments.source, imageManager, disposables)
        return ViewModelProviders.of(fragment, factory).get(OverlayImageFragmentViewModel::class.java)
    }
}