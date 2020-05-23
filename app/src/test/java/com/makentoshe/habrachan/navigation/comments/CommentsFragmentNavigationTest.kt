package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.BaseTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class CommentsFragmentNavigationTest : BaseTest() {

    private lateinit var commentsFragmentNavigation: CommentsFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        commentsFragmentNavigation = CommentsFragmentNavigation(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        commentsFragmentNavigation.back()

        verify { mockRouter.exit() }
    }

}