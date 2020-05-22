package com.makentoshe.habrachan.navigation.images

import com.makentoshe.habrachan.navigation.Router

class PostImageNavigator(private val router: Router) {

    fun back() = router.exit()
}