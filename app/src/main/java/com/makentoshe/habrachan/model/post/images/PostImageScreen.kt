package com.makentoshe.habrachan.model.post.images

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.post.images.PostImageFragmentPage
import com.makentoshe.habrachan.view.post.images.PostImagesFragment

class PostImagesScreen(
    private val index: Int,
    private val sources: Array<String>
) : Screen() {
    override val fragment: Fragment
        get() = PostImageFragmentPage.Factory().build(index, sources[index])
}