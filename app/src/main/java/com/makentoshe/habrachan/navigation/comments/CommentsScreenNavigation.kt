package com.makentoshe.habrachan.navigation.comments

import ru.terrakok.cicerone.Router

class CommentsScreenNavigation(private val router: Router) {
    fun back() = router.exit()
}