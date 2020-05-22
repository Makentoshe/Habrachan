package com.makentoshe.habrachan.navigation.comments

import ru.terrakok.cicerone.Router

class CommentsNavigator(private val router: Router) {
    fun back() = router.exit()
}