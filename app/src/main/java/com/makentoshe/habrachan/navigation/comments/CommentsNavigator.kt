package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.navigation.Router

class CommentsNavigator(private val router: Router) {
    fun back() = router.exit()
}