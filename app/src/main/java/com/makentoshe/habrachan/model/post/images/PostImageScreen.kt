package com.makentoshe.habrachan.model.post.images

import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.post.images.PostImageFragmentPage

class PostImageScreen(
    private val index: Int,
    private val sources: Array<String>
) : Screen() {
    override val fragment: PostImageFragmentPage
        get() = PostImageFragmentPage.Factory().build(index, sources[index])
}