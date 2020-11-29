package com.makentoshe.habrachan.navigation.images

import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.images.OverlayImageFragment

/** Screen displays an image used in publication for browsing or downloading */
class OverlayImageScreen(private val source: String) : Screen() {
    override fun getFragment() = OverlayImageFragment.Factory().build(source)
}
