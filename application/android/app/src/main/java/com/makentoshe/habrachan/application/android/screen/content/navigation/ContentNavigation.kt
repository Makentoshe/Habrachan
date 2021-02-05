package com.makentoshe.habrachan.application.android.screen.content.navigation

import ru.terrakok.cicerone.Router

class ContentNavigation(private val router: Router) {

    fun back() {
        router.exit()
    }
}