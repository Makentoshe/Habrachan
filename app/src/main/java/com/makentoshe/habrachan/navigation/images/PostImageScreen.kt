package com.makentoshe.habrachan.navigation.images

import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.images.PostImageFragmentPage

/** Screen displays an image used in publication for browsing or downloading */
class PostImageScreen(private val source: String) : Screen() {
    override fun getFragment() = PostImageFragmentPage.Factory().build(source)
}
