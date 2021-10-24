import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.application.android.common.AndroidUserSession2
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.R
import com.makentoshe.habrachan.application.android.screen.articles.flow.di.ArticlesFlowScope
import com.makentoshe.habrachan.application.android.screen.articles.flow.di.provider.ArticlesFlowAdapterProvider
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.TabLayoutMediatorController
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.MeScreenNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.smoothie.lifecycle.closeOnDestroy

@RunWith(AndroidJUnit4::class)
class ArticlesFlowFragmentTest {

    private var articlesFlowAdapterProvider = mockk<ArticlesFlowAdapterProvider>()
    private var articlesFlowAdapter = mockk<ArticlesFlowAdapter>(relaxed = true)
    private val meScreenNavigator = mockk<MeScreenNavigator>()
    private val loginScreenNavigator = mockk<LoginScreenNavigator>()
    private val androidUserSession2 = mockk<AndroidUserSession2>(relaxed = true)
    private val tabLayoutMediatorController = mockk<TabLayoutMediatorController>(relaxed = true)

    init {
        every { articlesFlowAdapterProvider.get() } returns articlesFlowAdapter
    }

    private val defaultModule = module {
        bind<ArticlesFlowAdapterProvider>().toInstance(articlesFlowAdapterProvider)
        bind<MeScreenNavigator>().toInstance(meScreenNavigator)
        bind<LoginScreenNavigator>().toInstance(loginScreenNavigator)
        bind<AndroidUserSession2>().toInstance(androidUserSession2)
        bind<TabLayoutMediatorController>().toInstance(tabLayoutMediatorController)
    }

    private val toothpickRootScope = Toothpick.openScope(ApplicationScope::class)
    private val toothpickScope = toothpickRootScope.openSubScope(ArticlesFlowScope::class).installModules(defaultModule)

    @After
    fun after() {
        Toothpick.closeScope(toothpickScope)
    }

    @Test
    fun testShouldCheckUserLoginIfUserLoggedIn() {
        every { androidUserSession2.isLoggedIn } returns true
        every { androidUserSession2.user?.login } returns "UnitTestLogin"

        val articlesFlowFragment = ArticlesFlowFragment.build(listOf(ArticlesUserSearch("Test", true)))
        launchFragmentInContainer(
            themeResId = R.style.Theme_MaterialComponents,
            fragmentArgs = articlesFlowFragment.requireArguments()
        ) {
            articlesFlowFragment.also { toothpickScope.closeOnDestroy(it).inject(it) }
        }.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
            assertEquals("UnitTestLogin", fragment.fragment_flow_articles_toolbar.subtitle)
        }.moveToState(Lifecycle.State.DESTROYED) // release di
    }

    @Test
    fun testShouldCheckUserLoginIfUserLoggedOff() {
        every { androidUserSession2.isLoggedIn } returns false

        val articlesFlowFragment = ArticlesFlowFragment.build(listOf(ArticlesUserSearch("Test", true)))
        launchFragmentInContainer(
            themeResId = R.style.Theme_MaterialComponents,
            fragmentArgs = articlesFlowFragment.requireArguments()
        ) {
            articlesFlowFragment.also { toothpickScope.closeOnDestroy(it).inject(it) }
        }.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
            assertEquals(null, fragment.fragment_flow_articles_toolbar.subtitle)
        }.moveToState(Lifecycle.State.DESTROYED) // release di
    }

    @Test
    fun testShouldCheckToolbarNavigationIconIfUserLoggedOff() {
        every { androidUserSession2.isLoggedIn } returns false

        val articlesFlowFragment = ArticlesFlowFragment.build(listOf(ArticlesUserSearch("Test", true)))
        launchFragmentInContainer(
            themeResId = R.style.Theme_MaterialComponents,
            fragmentArgs = articlesFlowFragment.requireArguments()
        ) {
            articlesFlowFragment.also { toothpickScope.closeOnDestroy(it).inject(it) }
        }.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
            assertEquals(R.drawable.ic_account_outline, fragment.fragment_flow_articles_toolbar.tag)
        }.moveToState(Lifecycle.State.DESTROYED) // release di
    }

    @Test
    fun testShouldCheckToolbarNavigationIconIfUserLoggedIn() {
        every { androidUserSession2.isLoggedIn } returns true

        val articlesFlowFragment = ArticlesFlowFragment.build(listOf(ArticlesUserSearch("Test", true)))
        launchFragmentInContainer(
            themeResId = R.style.Theme_MaterialComponents,
            fragmentArgs = articlesFlowFragment.requireArguments()
        ) {
            articlesFlowFragment.also { toothpickScope.closeOnDestroy(it).inject(it) }
        }.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
            assertEquals(R.drawable.ic_account, fragment.fragment_flow_articles_toolbar.tag)
        }.moveToState(Lifecycle.State.DESTROYED) // release di
    }

    @Test
    fun testShouldCheckViewPagerAdapter() {
        val articlesFlowFragment = ArticlesFlowFragment.build(listOf(ArticlesUserSearch("Test", true)))
        launchFragmentInContainer(
            themeResId = R.style.Theme_MaterialComponents,
            fragmentArgs = articlesFlowFragment.requireArguments()
        ) {
            articlesFlowFragment.also { toothpickScope.closeOnDestroy(it).inject(it) }
        }.moveToState(Lifecycle.State.RESUMED).onFragment { fragment ->
            assertEquals(articlesFlowAdapter, fragment.fragment_flow_articles_viewpager.adapter)
        }.moveToState(Lifecycle.State.DESTROYED) // release di
    }

    @Test
    fun testShouldCheckTabLayoutMediatorController() {
        val articlesFlowFragment = ArticlesFlowFragment.build(listOf(ArticlesUserSearch("Test", true)))
        launchFragmentInContainer(
            themeResId = R.style.Theme_MaterialComponents,
            fragmentArgs = articlesFlowFragment.requireArguments()
        ) {
            articlesFlowFragment.also { toothpickScope.closeOnDestroy(it).inject(it) }
        }.moveToState(Lifecycle.State.RESUMED).onFragment {
            verify { tabLayoutMediatorController.attach(any(), any()) }
        }.moveToState(Lifecycle.State.DESTROYED) // release di
    }

}
