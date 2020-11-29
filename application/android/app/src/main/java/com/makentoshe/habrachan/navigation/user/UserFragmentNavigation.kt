package com.makentoshe.habrachan.navigation.user

import ru.terrakok.cicerone.Router

class UserFragmentNavigation(private val router: Router) {
    fun back() = router.exit()
}