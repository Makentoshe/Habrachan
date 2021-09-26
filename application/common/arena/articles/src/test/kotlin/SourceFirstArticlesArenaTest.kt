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

class SourceFirstArticlesArenaTest {

    private val mockGetArticlesManager = mockk<GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>>()
    private val mockArenaStorage = mockk<ArenaCache<GetArticlesRequest2, GetArticlesResponse2>>()
    private val factory = ArticlesArena.Factory(mockGetArticlesManager, mockArenaStorage)

    private val mockRequest = mockk<GetArticlesRequest2>()
    private val mockResponse = mockk<GetArticlesResponse2>()

    @Test
    fun testShouldLoadFromSourceAndPutInCacheIfFetchResultSuccess() = runBlocking {
        coEvery { mockGetArticlesManager.articles(mockRequest) } returns Result.success(mockResponse)
        every { mockArenaStorage.carry(mockRequest, mockResponse) } just Runs

        val response = factory.sourceFirstArena().suspendFetch(mockRequest).getOrThrow()
        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldLoadFromCacheIfFetchResultFailure() = runBlocking {
        every { mockArenaStorage.fetch(mockRequest) } returns Result.success(mockResponse)
        coEvery { mockGetArticlesManager.articles(mockRequest) } returns Result.failure(Exception("Stub!"))

        val response = factory.sourceFirstArena().suspendFetch(mockRequest).getOrThrow()

        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldReturnArenaExceptionIfFetchResultFailureForCacheAndSource() = runBlocking {
        val cacheException = Exception("Cache")
        every { mockArenaStorage.fetch(any()) } returns Result.failure(cacheException)

        val sourceException = Exception("Source")
        coEvery { mockGetArticlesManager.articles(any()) } returns Result.failure(sourceException)

        val response = factory.sourceFirstArena().suspendFetch(mockk()).exceptionOrNull() as ArenaException

        assertEquals(cacheException, response.cacheException)
        assertEquals(sourceException, response.sourceException)
    }
}