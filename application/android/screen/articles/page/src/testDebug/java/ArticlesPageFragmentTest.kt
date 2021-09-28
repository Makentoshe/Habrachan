import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.network.request.SpecType
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import com.maketoshe.habrachan.application.android.screen.articles.page.R
import com.maketoshe.habrachan.application.android.screen.articles.page.di.ArticlesPageScope
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesPageAdapter
import io.mockk.*
import kotlinx.android.synthetic.main.fragment_page_articles.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.smoothie.lifecycle.closeOnDestroy
import java.net.UnknownHostException

@RunWith(AndroidJUnit4::class)
class ArticlesPageFragmentTest {

    private val mockExceptionHandler = mockk<ExceptionHandler>()
    private val mockGetArticlesViewModel = mockk<GetArticlesViewModel>(relaxed = true)
    private val mockArticlesPageAdapter = mockk<ArticlesPageAdapter>(relaxed = true)

    private val toothpickModule = module {
        bind<ExceptionHandler>().toInstance(mockExceptionHandler)
        bind<GetArticlesViewModel>().toInstance(mockGetArticlesViewModel)
        bind<ArticlesPageAdapter>().toInstance(mockArticlesPageAdapter)
    }

    private val spec = SpecType.Interesting
    private val toothpickScope = Toothpick.openScopes(
        ApplicationScope::class, ArticlesScope::class, ArticlesPageScope::class
    ).openSubScope(ArticlesPageScope(spec)).installModules(toothpickModule)

    @Before
    fun before() {
        `leave adapter empty`()
    }

    @Test
    fun `test should check progress bar visibility on initial state`() {
        `apply any data for the adapter`()

        `launch and resume fragment than execute` {
            fragment_page_articles_progress `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check progress bar visibility on loading state initially`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_progress `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check progress bar visibility on loading state pagination`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `content state`()
            `fill adapter with something`()
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_progress `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check progress bar visibility on content state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `content state`()
            fragment_page_articles_progress `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check progress bar visibility on error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `error state`()
            fragment_page_articles_progress `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check swipe-to-refresh visibility on initial state`() {
        `apply any data for the adapter`()

        `launch and resume fragment than execute` {
            fragment_page_articles_swipe `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check swipe-to-refresh visibility on loading state initially`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_swipe `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check swipe-to-refresh visibility on loading state pagination`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `content state`()
            `fill adapter with something`()
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_swipe `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check swipe-to-refresh visibility on content state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `content state`()
            fragment_page_articles_swipe `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check swipe-to-refresh visibility on error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`()
            fragment_page_articles_swipe `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check retry button visibility on initial state`() {
        `apply any data for the adapter`()

        `launch and resume fragment than execute` {

            fragment_page_articles_retry `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check retry button visibility on loading state initially`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `loading state`()
            fragment_page_articles_retry `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check retry button visibility on loading state pagination`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `content state`()
            `fill adapter with something`()
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_retry `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check retry button visibility on content state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `content state`()
            fragment_page_articles_retry `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check retry button visibility on error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`()
            fragment_page_articles_retry `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check retry button clickability on error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`()
            fragment_page_articles_retry perform ShortClick
            `verify adapter retrying`()
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message visibility on initial state`() {
        `apply any data for the adapter`()

        `launch and resume fragment than execute` {

            fragment_page_articles_message `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message visibility on loading state initially`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `loading state`()
            fragment_page_articles_message `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message visibility on loading state pagination`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `content state`()
            `fill adapter with something`()
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_message `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message visibility on content state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `content state`()
            fragment_page_articles_message `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message visibility on error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`()
            fragment_page_articles_message `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message text on undefined error state`() {
        val slot = `inject capturing slot for adapter`()
        val exception = `undefined exception`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`(exception)
            fragment_page_articles_message `text should be` exception.toString()
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception message text on known error state`() {
        val exceptionEntry = ExceptionEntry("Title", "Exception handler test")
        val slot = `inject capturing slot for adapter`()
        val exception = `internet exception`() `also bind to exception handler and return` exceptionEntry

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`(exception)
            fragment_page_articles_message `text should be` exceptionEntry.message
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception title visibility on initial state`() {
        `apply any data for the adapter`()

        `launch and resume fragment than execute` {

            fragment_page_articles_title `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception title visibility on loading state initially`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `loading state`()
            fragment_page_articles_title `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception title visibility on loading state pagination`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {
            slot.captured `trigger with` `content state`()
            `fill adapter with something`()
            slot.captured `trigger with` `loading state`()
            fragment_page_articles_title `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception title visibility on content state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `content state`()
            fragment_page_articles_title `visibility should be` View.GONE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception title visibility on error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`()
            fragment_page_articles_title `visibility should be` View.VISIBLE
        }.`and release at finish`()
    }

    @Test
    fun `test should check exception title text on undefined error state`() {
        val slot = `inject capturing slot for adapter`()

        `launch and resume fragment than execute` {

            slot.captured `trigger with` `error state`()
            fragment_page_articles_title `text should be` R.string.articles_initial_exception_title
        }.`and release at finish`()
    }

    private fun `launch and resume fragment than execute`(
        action: ArticlesPageFragment.() -> Unit
    ): FragmentScenario<ArticlesPageFragment> {
        return ArticlesPageFragment.build(SpecType.All).`launch this fragment and execute` {
            toothpickScope.closeOnDestroy(this).inject(this)
        }.`then resume and execute`(action)
    }

    private fun `inject capturing slot for adapter`(): CapturingSlot<(CombinedLoadStates) -> Unit> {
        val slot = slot<(CombinedLoadStates) -> Unit>()
        every { mockArticlesPageAdapter.addLoadStateListener(capture(slot)) } just Runs
        return slot
    }

    private infix fun ((CombinedLoadStates) -> Unit).`trigger with`(states: CombinedLoadStates) = invoke(states)

    private fun `undefined exception`() = ArenaException()

    private fun `internet exception`() = ArenaException(sourceException = Exception(UnknownHostException("test.host")))

    private infix fun ArenaException.`also bind to exception handler and return`(entry: ExceptionEntry): ArenaException {
        every { mockExceptionHandler.handle(any<Throwable>()) } returns entry
        return this
    }

    private fun `error state`(exception: Throwable = `undefined exception`()): CombinedLoadStates {
        val mockLoadState = mockk<CombinedLoadStates>(relaxed = true)
        every { mockLoadState.refresh } returns LoadState.Error(exception)
        return mockLoadState
    }

    private fun `loading state`(): CombinedLoadStates {
        val mockLoadState = mockk<CombinedLoadStates>(relaxed = true)
        every { mockLoadState.refresh } returns LoadState.Loading
        return mockLoadState
    }

    private fun `content state`(): CombinedLoadStates {
        val mockLoadState = mockk<CombinedLoadStates>(relaxed = true)
        every { mockLoadState.refresh } returns LoadState.NotLoading(false)
        return mockLoadState
    }

    private fun `apply any data for the adapter`() {
        coEvery { mockArticlesPageAdapter.submitData(any()) } just Runs
    }

    private fun `verify adapter retrying`() {
        verify { mockArticlesPageAdapter.retry() }
    }
    
    private fun `leave adapter empty`() {
        every { mockArticlesPageAdapter.itemCount } returns 0
    }

    private fun `fill adapter with something`() {
        every { mockArticlesPageAdapter.itemCount } returns 39
    }

}
