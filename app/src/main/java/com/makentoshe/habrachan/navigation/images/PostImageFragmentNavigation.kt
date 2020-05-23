package com.makentoshe.habrachan.navigation.images

import ru.terrakok.cicerone.Router

class PostImageFragmentNavigation(private val router: Router) {

    fun back() = router.exit()
}