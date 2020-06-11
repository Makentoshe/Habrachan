package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.BaseTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class CommentsFlowScreenNavigationTest : BaseTest() {

    private lateinit var commentsScreenNavigation: CommentsScreenNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        commentsScreenNavigation = CommentsScreenNavigation(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        commentsScreenNavigation.back()

        verify { mockRouter.exit() }
    }

}