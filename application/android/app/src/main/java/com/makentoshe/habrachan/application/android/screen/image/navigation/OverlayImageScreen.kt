package com.makentoshe.habrachan.application.android.screen.image.navigation

import com.makentoshe.habrachan.application.android.screen.image.OverlayImageFragment
import com.makentoshe.habrachan.navigation.Screen

/** Screen displays an image used in publication for browsing or downloading */
class OverlayImageScreen(private val source: String) : Screen() {
    override fun getFragment() = OverlayImageFragment.build(source)
}
