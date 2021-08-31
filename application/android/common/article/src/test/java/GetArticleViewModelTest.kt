import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleSpec
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
class GetArticleViewModelTest {

    @get:Rule
    internal var instantExecutorRule = InstantTaskExecutorRule()

    private val getArticleSpec = GetArticleSpec(articleId(1))

    private val mockUserSession = mockk<UserSession>()
    private val mockArticleArena = mockk<ArticleArena>()
    private val mockGetArticleRequest = mockk<GetArticleRequest2>()
    private val mockGetArticleResponse = mockk<GetArticleResponse2>()

    @Test
    fun testShouldReturnModelWithResponseOnSuccess() = runBlocking {
        every { mockArticleArena.request(mockUserSession, getArticleSpec.articleId) } returns mockGetArticleRequest
        coEvery { mockArticleArena.suspendFetch(mockGetArticleRequest) } returns Result.success(mockGetArticleResponse)

        val viewModel = GetArticleViewModel(mockUserSession, mockArticleArena, Option.Value(getArticleSpec))
        val model = viewModel.model.first()

        assertEquals(mockGetArticleResponse, model.getOrThrow().response2)
    }

    @Test
    fun testShouldReturnExceptionOnFailure() = runBlocking {
        val exception = Exception("Stub!")
        every { mockArticleArena.request(mockUserSession, getArticleSpec.articleId) } returns mockGetArticleRequest
        coEvery { mockArticleArena.suspendFetch(mockGetArticleRequest) } returns Result.failure(exception)

        val viewModel = GetArticleViewModel(mockUserSession, mockArticleArena, Option.Value(getArticleSpec))
        val model = viewModel.model.first()

        assertEquals(exception, model.exceptionOrNull() as Exception)
    }

    @Test(timeout = 500, expected = TestTimedOutException::class) // Job has not completed yet
    fun testShouldNotReturnAModelIfInitialOptionIsNone() = runBlocking {
        GetArticleViewModel(mockUserSession, mockArticleArena, Option.None).model.collect()
    }

    @Test
    fun testShouldReturnModelWithResponseOnSuccessChannel() = runBlocking {
        every { mockArticleArena.request(mockUserSession, getArticleSpec.articleId) } returns mockGetArticleRequest
        coEvery { mockArticleArena.suspendFetch(mockGetArticleRequest) } returns Result.success(mockGetArticleResponse)

        val viewModel = GetArticleViewModel(mockUserSession, mockArticleArena, Option.None)
        launch { viewModel.channel.send(getArticleSpec) }
        val model = viewModel.model.first()

        assertEquals(mockGetArticleResponse, model.getOrThrow().response2)
    }

    @Test
    fun testShouldReturnExceptionOnFailureChannel() = runBlocking {
        val exception = Exception("Stub!")
        every { mockArticleArena.request(mockUserSession, getArticleSpec.articleId) } returns mockGetArticleRequest
        coEvery { mockArticleArena.suspendFetch(mockGetArticleRequest) } returns Result.failure(exception)

        val viewModel = GetArticleViewModel(mockUserSession, mockArticleArena, Option.None)
        launch { viewModel.channel.send(getArticleSpec) }
        val model = viewModel.model.first()

        assertEquals(exception, model.exceptionOrNull() as Exception)
    }
}