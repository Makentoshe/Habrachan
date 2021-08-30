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

class CacheFirstArticleArenaTest {

    private val mockGetArticleManager = mockk<GetArticleManager<GetArticleRequest2>>()
    private val mockArenaStorage = mockk<ArenaCache<GetArticleRequest2, GetArticleResponse2>>()
    private val factory = ArticleArena.Factory(mockGetArticleManager, mockArenaStorage)

    @Test
    fun testShouldLoadFromCacheIfFetchResultSuccess() = runBlocking {
        val mockRequest = mockk<GetArticleRequest2>()
        val mockResponse = mockk<GetArticleResponse2>()
        every { mockArenaStorage.fetch(mockRequest) } returns Result.success(mockResponse)

        val response = factory.cacheFirstArena().suspendFetch(mockRequest).getOrThrow()
        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldLoadFromSourceAndPutInCacheIfFetchResultFailure() = runBlocking {
        val mockRequest = mockk<GetArticleRequest2>()
        every { mockArenaStorage.fetch(mockRequest) } returns Result.failure(Exception("Stub!"))

        val mockResponse = mockk<GetArticleResponse2>()
        coEvery { mockGetArticleManager.article(mockRequest) } returns Result.success(mockResponse)

        every { mockArenaStorage.carry(mockRequest, mockResponse) } just Runs

        val response = factory.cacheFirstArena().suspendFetch(mockRequest).getOrThrow()

        assertEquals(mockResponse, response)
    }

    @Test
    fun testShouldReturnArenaExceptionIfFetchResultFailureForCacheAndSource() = runBlocking {
        val cacheException = Exception("Cache")
        every { mockArenaStorage.fetch(any()) } returns Result.failure(cacheException)

        val sourceException = Exception("Source")
        coEvery { mockGetArticleManager.article(any()) } returns Result.failure(sourceException)

        val response = factory.cacheFirstArena().suspendFetch(mockk()).exceptionOrNull() as ArenaException

        assertEquals(cacheException, response.cacheException)
        assertEquals(sourceException, response.sourceException)
    }
}