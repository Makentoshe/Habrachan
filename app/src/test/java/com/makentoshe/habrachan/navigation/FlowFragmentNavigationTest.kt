package com.makentoshe.habrachan.navigation

import com.makentoshe.habrachan.BaseTest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

abstract class FlowFragmentNavigationTest : BaseTest() {

    protected lateinit var flowFragmentNavigation: FlowFragmentNavigation

    protected val mockNavigatorHolder = mockk<NavigatorHolder>(relaxed = true)
    protected val mockNavigator = mockk<Navigator>(relaxed = true)

    @Test
    fun testShouldSetNavigatorOnInit() {
        flowFragmentNavigation.init()

        verify { mockNavigatorHolder.setNavigator(mockNavigator) }
    }

    @Test
    fun testShouldRemoveNavigatorOnRelease() {
        flowFragmentNavigation.release()

        verify { mockNavigatorHolder.removeNavigator() }
    }

}