import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.SpecType
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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

    private val getArticleSpec = GetArticlesSpec(2, SpecType.All, 500)

    private val mockUserSession = mockk<UserSession>()
    private val mockArticlesArena = mockk<ArticlesArena>()

    @Test
    fun testShouldReturnModelInitially() = runBlocking {
        val viewModel = GetArticlesViewModel(mockUserSession, mockArticlesArena, Option.Value(getArticleSpec))
        val model = viewModel.model.first()

        assertEquals(getArticleSpec, model.spec)
    }

    @Test
    fun testShouldReturnModelByChannelInvoke() = runBlocking {
        val viewModel = GetArticlesViewModel(mockUserSession, mockArticlesArena, Option.None)
        launch { viewModel.channel.send(getArticleSpec) }
        val model = viewModel.model.first()

        assertEquals(getArticleSpec, model.spec)
    }

    @Test(timeout = 500, expected = TestTimedOutException::class) // Job has not completed yet
    fun testShouldNotReturnModel() = runBlocking {
        GetArticlesViewModel(mockUserSession, mockArticlesArena, Option.None).model.first(); Unit
    }

}