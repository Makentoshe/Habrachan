package com.makentoshe.habrachan.navigation.comments

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class CommentsNavigatorTest {

    private lateinit var commentsNavigator: CommentsNavigator

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        commentsNavigator = CommentsNavigator(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        commentsNavigator.back()

        verify { mockRouter.exit() }
    }

}