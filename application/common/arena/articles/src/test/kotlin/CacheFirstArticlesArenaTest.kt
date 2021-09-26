import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CacheFirstArticlesArenaTest {

    private val mockGetArticleManager = mockk<GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>>()
    private val mockArenaStorage = mockk<ArenaCache<GetArticlesRequest2, GetArticlesResponse2>>()
    private val factory = ArticlesArena.Factory(mockGetArticleManager, mockArenaStorage)

    @Test
    fun testShouldLoadFromCacheIfFetchResultSuccess() = runBlocking {
        val mockRequest = mockk<GetArticlesRequest2>()
        val mockResponse = mockk<GetArticlesResponse2>()
        every { mockArenaStorage.fetch(mockRequest) } returns Result.success(mockResponse)

        val response = factory.cacheFirstArena().suspendFetch(mockRequest).getOrThrow()
        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldLoadFromSourceAndPutInCacheIfFetchResultFailure() = runBlocking {
        val mockRequest = mockk<GetArticlesRequest2>()
        every { mockArenaStorage.fetch(mockRequest) } returns Result.failure(Exception("Stub!"))

        val mockResponse = mockk<GetArticlesResponse2>()
        coEvery { mockGetArticleManager.articles(mockRequest) } returns Result.success(mockResponse)

        every { mockArenaStorage.carry(mockRequest, mockResponse) } just Runs

        val response = factory.cacheFirstArena().suspendFetch(mockRequest).getOrThrow()

        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldReturnArenaExceptionIfFetchResultFailureForCacheAndSource() = runBlocking {
        val cacheException = Exception("Cache")
        every { mockArenaStorage.fetch(any()) } returns Result.failure(cacheException)

        val sourceException = Exception("Source")
        coEvery { mockGetArticleManager.articles(any()) } returns Result.failure(sourceException)

        val response = factory.cacheFirstArena().suspendFetch(mockk()).exceptionOrNull() as ArenaException

        assertEquals(cacheException, response.cacheException)
        assertEquals(sourceException, response.sourceException)
    }
}