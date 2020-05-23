package com.makentoshe.habrachan.navigation.images

import com.makentoshe.habrachan.BaseTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class OverlayImageFragmentNavigationTest : BaseTest() {

    private lateinit var overlayImageFragmentNavigation: OverlayImageFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        overlayImageFragmentNavigation = OverlayImageFragmentNavigation(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        overlayImageFragmentNavigation.back()

        verify { mockRouter.exit() }
    }

}
