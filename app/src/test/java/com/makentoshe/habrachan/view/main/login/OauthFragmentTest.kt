package com.makentoshe.habrachan.view.main.login

import android.content.Intent
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.login.OauthFragmentScope
import com.makentoshe.habrachan.model.main.login.OauthType
import com.makentoshe.habrachan.navigation.main.login.OauthScreen
import com.makentoshe.habrachan.viewmodel.main.login.LoginViewModel
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class OauthFragmentTest : BaseRobolectricTest() {

    private val router by inject<Router>()
    private val mockViewModel = mockk<LoginViewModel>(relaxed = true)
    private val disposables = spyk(CompositeDisposable())

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java,
            OauthFragmentScope::class.java
        ).installTestModules(module {
            bind<LoginViewModel>().toInstance(mockViewModel)
            bind<CompositeDisposable>().toInstance(disposables)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(OauthFragmentScope::class.java)
    }

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    @Test
    fun testShouldClearDisposablesOnFragmentDestroy() {
        activityController.setup().get()
        router.navigateTo(OauthScreen(OauthType.Github))
        router.exit()

        verify { disposables.clear() }
    }
}