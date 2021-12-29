import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleSpec
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.usersession.*
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.request.ArticleVote
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class VoteArticleViewModelTest {

    @get:Rule
    internal var instantExecutorRule = InstantTaskExecutorRule()

    private val voteArticleSpec = VoteArticleSpec(articleId(1), ArticleVote.Up)

    private val mockVoteArticlesArena = mockk<VoteArticleArena>()
    private val mockDatabase = mockk<AndroidCacheDatabase>(relaxed = true)

    @Before
    fun before() {
        coEvery { mockVoteArticlesArena.suspendCarry(any()) } returns Result.success(mockk(relaxed = true))
        every { mockVoteArticlesArena.request(any(), any(), any()) } returns mockk(relaxed = true)
    }

    @Test
    fun testShouldReturnModelByChannelInvoke() = runBlocking {
        val viewModel = VoteArticleViewModel(`get deprecated user session provider`(), mockVoteArticlesArena, mockDatabase)
        launch { viewModel.channel.send(voteArticleSpec) }

        Assert.assertTrue(viewModel.model.firstOrNull()!!.isSuccess)
    }

    @Test
    fun testShouldUpdateDatabaseOnSuccess() = runBlocking {
        val mockDatabase = mockk<AndroidCacheDatabase>()
        every { mockDatabase.articlesDao().getById(any()) } returns mockk(relaxed = true)
        every { mockDatabase.articlesDao().insert(any()) } just Runs

        val viewModel = VoteArticleViewModel(`get deprecated user session provider`(), mockVoteArticlesArena, mockDatabase)
        launch { viewModel.channel.send(voteArticleSpec) }

        Assert.assertNotNull(viewModel.model.firstOrNull())
        verify { mockDatabase.articlesDao().insert(any()) }
    }

    @Test
    fun testShouldNotUpdateDatabaseOnFailure() = runBlocking {
        coEvery { mockVoteArticlesArena.suspendCarry(any()) } returns Result.failure(mockk(relaxed = true))

        val viewModel = VoteArticleViewModel(`get deprecated user session provider`(), mockVoteArticlesArena, mockDatabase)
        launch { viewModel.channel.send(voteArticleSpec) }

        Assert.assertNotNull(viewModel.model.firstOrNull())
        verify(inverse = true) { mockDatabase.articlesDao().insert(any()) }
    }

    private fun `get deprecated user session provider`(): AndroidUserSessionProvider {
        return mockk<AndroidUserSessionProvider>().apply {
            every { get() } returns `android user session`()
        }
    }

    private fun `android user session`() = AndroidUserSession(
        ClientId("client"),
        ClientApi(""),
        AccessToken("token"),
        HabrSessionIdCookie("habr-session-id"),
        ConnectSidCookie("connect-sid"),
    )

}
