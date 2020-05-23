package com.makentoshe.habrachan.navigation.images

import com.makentoshe.habrachan.BaseTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class PostImageFragmentNavigationTest : BaseTest() {

    private lateinit var commentsFragmentNavigation: PostImageFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        commentsFragmentNavigation = PostImageFragmentNavigation(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        commentsFragmentNavigation.back()

        verify { mockRouter.exit() }
    }

}
