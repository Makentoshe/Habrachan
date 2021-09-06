package com.makentoshe.habrachan.application.android.screen.content.di

import com.makentoshe.habrachan.application.android.arena.ContentArenaCache
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.content.ContentFragment
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentNavigation
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.GetContentManager
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class CommonContentModule(injectorScope: FragmentInjector.FragmentInjectorScope<ContentFragment>) : Module() {

    private val router by inject<Router>()
    private val cacheDatabase by inject<AndroidCacheDatabase>()
    private val getContentManager by inject<GetContentManager>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        bind<ContentNavigation>().toInstance(ContentNavigation(router))
        bind<com.makentoshe.habrachan.application.android.filesystem.FileSystem>().toInstance(com.makentoshe.habrachan.application.android.filesystem.FileSystem.pictures(injectorScope.context))

        val contentArenaCache = ContentArenaCache(cacheDatabase.contentDao(), injectorScope.context.cacheDir)
        val getContentArena = ContentArena(getContentManager, contentArenaCache)
        bind<ContentArena>().toInstance(getContentArena)
    }
}
