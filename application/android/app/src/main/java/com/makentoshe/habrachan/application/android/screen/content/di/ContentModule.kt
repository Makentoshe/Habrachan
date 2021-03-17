package com.makentoshe.habrachan.application.android.screen.content.di

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.arena.ContentArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.filesystem.FileSystem
import com.makentoshe.habrachan.application.android.screen.content.model.ContentActionBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentNavigation
import com.makentoshe.habrachan.application.android.screen.content.viewmodel.ContentViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetContentManager
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ContentScope

class ContentModule(fragment: CoreFragment) : Module() {

    private val router by inject<Router>()
    private val cacheDatabase by inject<AndroidCacheDatabase>()
    private val userSession by inject<UserSession>()

    private val getContentManager by inject<GetContentManager>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)
        bind<ContentActionBroadcastReceiver>().toInstance(ContentActionBroadcastReceiver(fragment.lifecycleScope))
        bind<ContentNavigation>().toInstance(ContentNavigation(router))

        val contentArenaCache = ContentArenaCache(cacheDatabase.contentDao(), fragment.requireContext().cacheDir)
        val factory = ContentViewModel.Factory(ContentArena(getContentManager, contentArenaCache), userSession)
        val viewModel = ViewModelProviders.of(fragment, factory)[ContentViewModel::class.java]
        bind<ContentViewModel>().toInstance(viewModel)

        val filesystem = FileSystem.pictures(fragment.requireContext()).also(::println)
        bind<FileSystem>().toInstance(filesystem)
    }
}
