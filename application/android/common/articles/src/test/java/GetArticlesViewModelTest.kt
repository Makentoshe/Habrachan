import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena3
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.model.TestTimedOutException
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class GetArticlesViewModelTest {

    @get:Rule
    internal var instantExecutorRule = InstantTaskExecutorRule()

    private val getArticleSpec = GetArticlesSpec(setOf(mockk(relaxed = true)))

    private val mockUserSession = mockk<UserSession>()
    private val mockArticlesArena = mockk<ArticlesArena3>()

    @Test
    fun testShouldReturnPagingDataInitially() = runBlocking {
        val viewModel = GetArticlesViewModel(mockUserSession, mockArticlesArena, Option.Value(getArticleSpec))
        assertNotNull(viewModel.pagingData.firstOrNull())
    }

    @Test
    fun testShouldReturnModelByChannelInvoke() = runBlocking {
        val viewModel = GetArticlesViewModel(mockUserSession, mockArticlesArena, Option.None)
        launch { viewModel.channel.send(getArticleSpec) }

        assertNotNull(viewModel.pagingData.firstOrNull())
    }

    @Test(timeout = 500, expected = TestTimedOutException::class) // Job has not completed yet
    fun testShouldNotReturnModel() = runBlocking {
        assertNull(GetArticlesViewModel(mockUserSession, mockArticlesArena, Option.None).pagingData.firstOrNull())
    }

}