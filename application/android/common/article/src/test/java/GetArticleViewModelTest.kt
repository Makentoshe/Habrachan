import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelRequest
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.*
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.article.get.ArticleFromArena
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArena
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArenaRequest
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArenaResponse
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.left
import com.makentoshe.habrachan.functional.rightOrNull
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonPrimitive
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

    @Test
    fun `test should collect model with response on success`() = runBlocking {
        val article = ArticleFromArena(mapOf("id" to JsonPrimitive(39)))
        val request = GetArticleArenaRequest(ArticleId(39), AdditionalRequestParameters())
        val response = flowOf(FlowArenaResponse<GetArticleArenaResponse, ArenaException>(true, Either2.Left(GetArticleArenaResponse(request, article))))
        val viewModel = `get article view model`(getArticleArena = `get article arena`(response), initial = Option2.from(GetArticleViewModelRequest(request.article)))

        val model = viewModel.model.first()
        assertEquals(true, model.loading)
        assertEquals(article, model.content.left().article)
    }

    @Test
    fun `test should collect exception entry on failure`() = runBlocking {
        val exception = Exception("Stub!")
        val request = GetArticleArenaRequest(ArticleId(39), AdditionalRequestParameters())
        val response = flowOf(FlowArenaResponse<GetArticleArenaResponse, ArenaException>(true, Either2.Right(ArenaException(exception))))
        val viewModel = `get article view model`(getArticleArena = `get article arena`(response), initial = Option2.from(GetArticleViewModelRequest(request.article)))

        val model = viewModel.model.first()
        assertEquals(true, model.loading)
        assertEquals(exception, model.content.rightOrNull()?.throwable?.cause)
    }

    @Test(timeout = 1000, expected = TestTimedOutException::class)
    fun `test shouldn't collect model on empty initial option`() = runBlocking {
        val response = flowOf(FlowArenaResponse<GetArticleArenaResponse, ArenaException>(true, Either2.Right(ArenaException(Exception("Stub!")))))
        `get article view model`(getArticleArena = `get article arena`(response)).model.first(); Unit
    }

    @Test
    fun `test should collect exception entry on failure due channel`() = runBlocking {
        val exception = Exception("Stub!")
        val request = GetArticleArenaRequest(ArticleId(39), AdditionalRequestParameters())
        val response = flowOf(FlowArenaResponse<GetArticleArenaResponse, ArenaException>(false, Either2.Right(ArenaException(exception))))
        val viewModel = `get article view model`(getArticleArena = `get article arena`(response))

        launch { viewModel.channel.send(GetArticleViewModelRequest(request.article)) }

        val model = viewModel.model.first()
        assertEquals(false, model.loading)
        assertEquals(exception, model.content.rightOrNull()?.throwable?.cause)
    }

    @Test
    fun `test should collect model on success due channel`() = runBlocking {
        val article = ArticleFromArena(mapOf("id" to JsonPrimitive(39)))
        val request = GetArticleArenaRequest(ArticleId(39), AdditionalRequestParameters())
        val response = flowOf(FlowArenaResponse<GetArticleArenaResponse, ArenaException>(true, Either2.Left(GetArticleArenaResponse(request, article))))
        val viewModel = `get article view model`(getArticleArena = `get article arena`(response))

        launch { viewModel.channel.send(GetArticleViewModelRequest(request.article)) }

        val model = viewModel.model.first()
        assertEquals(true, model.loading)
        assertEquals(article, model.content.left().article)
    }

    private fun `get article arena`(response: Flow<FlowArenaResponse<GetArticleArenaResponse, ArenaException>>): GetArticleArena {
        return mockk<GetArticleArena>().apply {
            coEvery { suspendFlowFetch(any()) } returns response
        }
    }

    private fun `get article view model`(
        stringsProvider: BundledStringsProvider = mockk(relaxed = true),
        userSessionProvider: AndroidUserSessionProvider = `get android user session provider`(),
        getArticleArena: GetArticleArena,
        initial: Option2<GetArticleViewModelRequest> = Option2.None
    ) = GetArticleViewModel(stringsProvider, userSessionProvider, getArticleArena, initial)

    private fun `get android user session provider`() = mockk<AndroidUserSessionProvider>().apply {
        every { get() } returns `android user session`()
    }

    private fun `android user session`() = AndroidUserSession(
        ClientId("client-id"),
        ClientApi("client-api"),
        AccessToken("token"),
        HabrSessionIdCookie("habr-session-id"),
        ConnectSidCookie("connect-sid"),
    )
}