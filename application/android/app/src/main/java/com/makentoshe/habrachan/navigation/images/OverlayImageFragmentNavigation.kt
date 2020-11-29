package com.makentoshe.habrachan.navigation.images

import ru.terrakok.cicerone.Router

class OverlayImageFragmentNavigation(private val router: Router) {

    fun back() = router.exit()
}