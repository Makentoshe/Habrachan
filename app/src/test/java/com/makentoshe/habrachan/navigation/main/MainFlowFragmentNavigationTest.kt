package com.makentoshe.habrachan.navigation.main

import com.makentoshe.habrachan.navigation.FlowFragmentNavigationTest
import com.makentoshe.habrachan.navigation.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.navigation.main.login.LoginFlowScreen
import com.makentoshe.habrachan.navigation.main.menu.MenuScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.terrakok.cicerone.Router

class MainFlowFragmentNavigationTest : FlowFragmentNavigationTest() {

    private lateinit var mainFlowFragmentNavigation: MainFlowFragmentNavigation

    private val mockRouter = mockk<Router>(relaxed = true)

    @Before
    fun before() {
        mainFlowFragmentNavigation = MainFlowFragmentNavigation(mockRouter, mockNavigatorHolder, mockNavigator)
        flowFragmentNavigation = mainFlowFragmentNavigation
    }

    @Test
    fun testShouldNavigateToLoginFlowScreen() {
        mainFlowFragmentNavigation.toLoginFlowScreen()

        verify { mockRouter.replaceScreen(withArg { it is LoginFlowScreen }) }
    }

    @Test
    fun testShouldNavigateToArticlesScreen() {
        mainFlowFragmentNavigation.toArticlesScreen(0)

        verify { mockRouter.replaceScreen(withArg { it is ArticlesFlowScreen }) }
    }

    @Test
    fun testShouldNavigateToMenuScreen() {
        mainFlowFragmentNavigation.toMenuScreen()

        verify { mockRouter.replaceScreen(withArg { it is MenuScreen }) }
    }

}