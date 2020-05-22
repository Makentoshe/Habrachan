package com.makentoshe.habrachan.navigation.user

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class UserNavigatorTest {

    private lateinit var userNavigator: UserNavigator

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        userNavigator = UserNavigator(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        userNavigator.back()

        verify { mockRouter.exit() }
    }

}
