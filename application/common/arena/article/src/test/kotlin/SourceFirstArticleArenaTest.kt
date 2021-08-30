import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SourceFirstArticleArenaTest {

    private val mockGetArticleManager = mockk<GetArticleManager<GetArticleRequest2>>()
    private val mockArenaStorage = mockk<ArenaCache<GetArticleRequest2, GetArticleResponse2>>()
    private val factory = ArticleArena.Factory(mockGetArticleManager, mockArenaStorage)

    private val mockRequest = mockk<GetArticleRequest2>()
    private val mockResponse = mockk<GetArticleResponse2>()

    @Test
    fun testShouldLoadFromSourceAndPutInCacheIfFetchResultSuccess() = runBlocking {
        coEvery { mockGetArticleManager.article(mockRequest) } returns Result.success(mockResponse)
        every { mockArenaStorage.carry(mockRequest, mockResponse) } just Runs

        val response = factory.sourceFirstArena().suspendFetch(mockRequest).getOrThrow()
        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldLoadFromCacheIfFetchResultFailure() = runBlocking {
        every { mockArenaStorage.fetch(mockRequest) } returns Result.success(mockResponse)
        coEvery { mockGetArticleManager.article(mockRequest) } returns Result.failure(Exception("Stub!"))

        val response = factory.sourceFirstArena().suspendFetch(mockRequest).getOrThrow()

        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldReturnArenaExceptionIfFetchResultFailureForCacheAndSource() = runBlocking {
        val cacheException = Exception("Cache")
        every { mockArenaStorage.fetch(any()) } returns Result.failure(cacheException)

        val sourceException = Exception("Source")
        coEvery { mockGetArticleManager.article(any()) } returns Result.failure(sourceException)

        val response = factory.sourceFirstArena().suspendFetch(mockk()).exceptionOrNull() as ArenaException

        assertEquals(cacheException, response.cacheException)
        assertEquals(sourceException, response.sourceException)
    }
}