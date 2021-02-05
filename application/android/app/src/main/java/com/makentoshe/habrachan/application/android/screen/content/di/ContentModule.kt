package com.makentoshe.habrachan.application.android.screen.content.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.ContentImageArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.content.ContentFragmentPage
import com.makentoshe.habrachan.application.android.screen.content.viewmodel.ContentViewModel
import com.makentoshe.habrachan.application.core.arena.image.ImageArena
import com.makentoshe.habrachan.network.manager.ImageManager
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ContentScope

class ContentModule(fragment: ContentFragmentPage) : Module() {

    private val client by inject<OkHttpClient>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)

        val imageArena = ImageArena(ImageManager.Builder(client).build(), ContentImageArenaCache())
        val factory = ContentViewModel.Factory(imageArena)
        val viewModel = ViewModelProviders.of(fragment, factory)[ContentViewModel::class.java]
        bind<ContentViewModel>().toInstance(viewModel)
    }
}
