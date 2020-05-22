package com.makentoshe.habrachan.view.main.login

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.navigation.main.login.LoginScreen
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.main.login.LoginFlowNavigator
import com.makentoshe.habrachan.navigation.user.UserScreen
import io.mockk.*
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

class LoginFlowFragmentNavigatorTest: BaseTest() {

    private lateinit var navigator: LoginFlowNavigator

    private val mockRouter = mockk<Router>(relaxed = true)
    private val mockNavigatorHolder = mockk<NavigatorHolder>(relaxed = true)
    private val mockNavigator = mockk<Navigator>(relaxed = true)

    @Before
    fun before() {
         navigator = LoginFlowNavigator(mockRouter, mockNavigatorHolder, mockNavigator)
    }

    @Test
    fun testShouldInvokeSetNavigatorOnInit() {
        navigator.init()
        verify { mockNavigatorHolder.setNavigator(mockNavigator) }
    }

    @Test
    fun testShouldInvokeRemoveNavigatorOnRelease() {
        navigator.release()
        verify { mockNavigatorHolder.removeNavigator() }
    }

    @Test
    fun testShouldRouteToLoginScreen() {
        navigator.toLoginScreen()
        verify { mockRouter.replaceScreen(capture(slot<LoginScreen>())) }
    }

    @Test
    fun testShouldRouteToUserScreen() {
        navigator.toUserScreen(UserAccount.Me)
        verify { mockRouter.replaceScreen(capture(slot<UserScreen>())) }
    }
}