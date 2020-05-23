package com.makentoshe.habrachan.navigation.main.login

import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.FlowFragmentNavigationTest
import com.makentoshe.habrachan.navigation.user.UserScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class LoginFlowFragmentNavigationTest : FlowFragmentNavigationTest() {

    private lateinit var loginFlowFragmentNavigation: LoginFlowFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        loginFlowFragmentNavigation = LoginFlowFragmentNavigation(mockRouter, mockNavigatorHolder, mockNavigator)
        flowFragmentNavigation = loginFlowFragmentNavigation
    }

    @Test
    fun testShouldNavigateToLoginScreen() {
        loginFlowFragmentNavigation.toLoginScreen()

        verify { mockRouter.replaceScreen(withArg { it is LoginScreen }) }
    }

    @Test
    fun testShouldNavigateToUserScreen() {
        loginFlowFragmentNavigation.toUserScreen(UserAccount.Me)

        verify { mockRouter.replaceScreen(withArg { it is UserScreen }) }
    }

}