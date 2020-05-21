package com.makentoshe.habrachan.view.main.login

import android.content.Intent
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.login.LoginFlowFragmentScope
import com.makentoshe.habrachan.navigation.main.login.LoginFlowScreen
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class LoginFlowFragmentTest : BaseRobolectricTest() {

    private val mockViewModel = mockk<LoginViewModel>(relaxed = true)
    private val spyDisposables = spyk(CompositeDisposable())
    private val mockNavigator = mockk<LoginFlowFragment.Navigator>(relaxed = true)
    private val mockSessionDatabase = mockk<SessionDatabase>(relaxed = true)
    private val mockLogoutBroadcastReceiver = mockk<LogoutBroadcastReceiver>(relaxed = true)

    private val router by inject<Router>()

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java, LoginFlowFragmentScope::class.java
        ).installTestModules(module {
            bind<LoginViewModel>().toInstance(mockViewModel)
            bind<CompositeDisposable>().toInstance(spyDisposables)
            bind<LoginFlowFragment.Navigator>().toInstance(mockNavigator)
            bind<SessionDatabase>().toInstance(mockSessionDatabase)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(LoginFlowFragmentScope::class.java)
    }

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    @Test
    fun testShouldDisplayLoginFragmentAfterLogoutIntent() {
        val logoutObservable = BehaviorSubject.create<Unit>()
        every { mockLogoutBroadcastReceiver.observable } returns logoutObservable

        activityController.setup().get()
        router.navigateTo(LoginFlowScreen())

        logoutObservable.onNext(Unit)

        verify(exactly = 1) { mockNavigator.toLoginScreen() }
    }

    @Test
    fun testShouldInitAndReleaseNavigatorOnLifecycleEvents() {
        activityController.setup()
        router.navigateTo(LoginFlowScreen())
        verify(exactly = 1) { mockNavigator.init() }
        verify(exactly = 0) { mockNavigator.release() }
        activityController.stop()
        verify(exactly = 1) { mockNavigator.release() }
    }

    @Test
    fun testShouldDisplayUserFragmentAfterSuccessLogin() {
        val loginObservable = BehaviorSubject.create<LoginResponse>()
        every { mockViewModel.loginObservable } returns loginObservable

        activityController.setup().get()
        router.navigateTo(LoginFlowScreen())

        loginObservable.onNext(mockk<LoginResponse.Success>())

        verify { mockNavigator.toUserScreen(UserAccount.Me) }
    }

    @Test
    fun testShouldDisplayUserFragmentIfUserLoggedIn() {
        every { mockSessionDatabase.session().get().isLoggedIn } returns true

        activityController.setup().get()
        router.navigateTo(LoginFlowScreen())

        verify { mockNavigator.toUserScreen(UserAccount.Me) }
    }

    @Test
    fun testShouldDisplayLoginFragmentIfUserLoggedOff() {
        every { mockSessionDatabase.session().get().isLoggedIn } returns false

        activityController.setup().get()
        router.navigateTo(LoginFlowScreen())

        verify { mockNavigator.toLoginScreen() }
    }

    @Test
    fun testShouldCheckDisposablesClearedOnFragmentDestroy() {
        activityController.setup().get()
        router.navigateTo(LoginFlowScreen())
        router.exit()

        verify { spyDisposables.clear() }
    }

}