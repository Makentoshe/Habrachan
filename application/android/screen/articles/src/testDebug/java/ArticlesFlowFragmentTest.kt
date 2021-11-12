import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSession
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.isUserLoggedIn
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.R
import com.makentoshe.habrachan.application.android.screen.articles.di.provider.ArticlesFlowAdapterProvider
import com.makentoshe.habrachan.application.android.screen.articles.di.scope.ArticlesFlowScope
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.articles.model.TabLayoutMediatorController
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.MeScreenNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import org.junit.After
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
    private val androidUserSession = mockk<AndroidUserSession>(relaxed = true)
    private val tabLayoutMediatorController = mockk<TabLayoutMediatorController>(relaxed = true)

    init {
        every { articlesFlowAdapterProvider.get() } returns articlesFlowAdapter
    }

    private val defaultModule = module {
        bind<ArticlesFlowAdapterProvider>().toInstance(articlesFlowAdapterProvider)
        bind<MeScreenNavigator>().toInstance(meScreenNavigator)
        bind<LoginScreenNavigator>().toInstance(loginScreenNavigator)
        bind<AndroidUserSessionProvider>().toInstance(object : AndroidUserSessionProvider {
            override fun get() = androidUserSession
        })
        bind<TabLayoutMediatorController>().toInstance(tabLayoutMediatorController)
    }

    private val toothpickRootScope = Toothpick.openScope(ApplicationScope::class)
    private val toothpickScope = toothpickRootScope.openSubScope(ArticlesFlowScope::class).installModules(defaultModule)

    @After
    fun after() {
        Toothpick.closeScope(toothpickScope)
    }

    @Test
    fun `test should check toolbar navigation tag for logged off user`() {
        `log off from user account`()

        `get default ArticlesFlowFragment instance`().`launch this fragment and execute` {
            toothpickScope.closeOnDestroy(this).inject(this)
        }.`then resume and execute` {
            fragment_flow_articles_toolbar `tag should be` R.drawable.ic_account_outline
        }.`and release at finish`()
    }

    @Test
    fun `test should check toolbar navigation tag for logged in user`() {
        `log on into user account`()

        `get default ArticlesFlowFragment instance`().`launch this fragment and execute` {
            toothpickScope.closeOnDestroy(this).inject(this)
        }.`then resume and execute` {
            println(R.drawable.ic_account_outline)
            fragment_flow_articles_toolbar `tag should be` R.drawable.ic_account
        }.`and release at finish`()
    }

    @Test
    fun `test should check view pager adapter`() {
        `get default ArticlesFlowFragment instance`().`launch this fragment and execute` {
            toothpickScope.closeOnDestroy(this).inject(this)
        }.`then resume and execute` {
            fragment_flow_articles_viewpager `should have adapter` articlesFlowAdapter
        }.`and release at finish`()
    }

    @Test
    fun `test should check tab layout mediator controller`() {
        `get default ArticlesFlowFragment instance`().`launch this fragment and execute` {
            toothpickScope.closeOnDestroy(this).inject(this)
        }.`then resume and execute` {
            verify { tabLayoutMediatorController.attach(any(), any()) }
        }.`and release at finish`()
    }

    private fun `get default ArticlesFlowFragment instance`(
        `with searches`: List<ArticlesUserSearch> = listOf(ArticlesUserSearch("Test", true))
    ) = ArticlesFlowFragment.build(`with searches`)

    private fun `log off from user account`() {
        mockkStatic(Class.forName("com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionKt").kotlin)
        every { androidUserSession.isUserLoggedIn } returns false
    }

    private fun `log on into user account`() {
        mockkStatic(Class.forName("com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionKt").kotlin)
        every { androidUserSession.isUserLoggedIn } returns true
    }

}