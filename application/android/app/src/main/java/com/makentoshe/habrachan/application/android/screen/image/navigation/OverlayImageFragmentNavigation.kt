package com.makentoshe.habrachan.application.android.screen.image.navigation

import ru.terrakok.cicerone.Router

class OverlayImageFragmentNavigation(private val router: Router) {

    fun back() = router.exit()
}