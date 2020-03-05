package com.makentoshe.habrachan.model.article.images

import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.article.images.PostImageFragmentPage

/** Screen displays an image used in publication for browsing or downloading */
class PostImageScreen(private val source: String) : Screen() {
    override val fragment: PostImageFragmentPage
        get() = PostImageFragmentPage.Factory().build(source)
}