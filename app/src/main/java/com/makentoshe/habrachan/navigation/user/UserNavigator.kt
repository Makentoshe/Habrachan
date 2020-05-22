package com.makentoshe.habrachan.navigation.user

import com.makentoshe.habrachan.navigation.Router

class UserNavigator(private val router: Router) {
    fun back() = router.exit()
}