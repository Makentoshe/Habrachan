package com.makentoshe.habrachan.model.post.images

import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.post.images.PostImageFragmentPage

/** Screen displays an image used in publication for browsing or downloading */
class PostImageScreen(private val source: String) : Screen() {
    override val fragment: PostImageFragmentPage
        get() = PostImageFragmentPage.Factory().build(source)
}