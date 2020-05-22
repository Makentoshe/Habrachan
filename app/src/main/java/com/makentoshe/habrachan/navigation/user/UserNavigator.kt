package com.makentoshe.habrachan.navigation.user

import ru.terrakok.cicerone.Router

class UserNavigator(private val router: Router) {
    fun back() = router.exit()
}