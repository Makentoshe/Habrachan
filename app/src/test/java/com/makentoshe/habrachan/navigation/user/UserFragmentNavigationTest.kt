package com.makentoshe.habrachan.navigation.user

import com.makentoshe.habrachan.BaseTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class UserFragmentNavigationTest : BaseTest() {

    private lateinit var userFragmentNavigation: UserFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        userFragmentNavigation = UserFragmentNavigation(mockRouter)
    }

    @Test
    fun testShouldNavigateToBack() {
        userFragmentNavigation.back()

        verify { mockRouter.exit() }
    }

}
