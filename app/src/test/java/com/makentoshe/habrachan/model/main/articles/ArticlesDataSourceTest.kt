package com.makentoshe.habrachan.model.main.articles

import androidx.paging.PositionalDataSource
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class ArticlesDataSourceTest {

    private lateinit var dataSource: ArticlesDataSource
    private val manager = mockk<ArticlesManager>()
    private val cacheDatabase = mockk<HabrDatabase>(relaxed = true)
    private val imageDatabase = mockk<ImageDatabase>(relaxed = true)

    @Before
    fun before() {
        dataSource = ArticlesDataSource(manager, cacheDatabase, imageDatabase)
    }

    @Test
    fun testShouldReturnArticlesForInitialLoad() {
        val testInitialSuccessObservable = dataSource.initialSuccessObservable.test()

        val mockArticles =
            Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()

        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { mockArticlesResponse.data } returns mockArticles

        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)

        dataSource.loadInitial(params, callback)

        val slot = slot<List<Article>>()
        verify(exactly = 1) { callback.onResult(capture(slot), 0) }
        assertEquals(mockArticles, slot.captured)

        testInitialSuccessObservable.assertValue{ it.response == mockArticlesResponse }.dispose()
    }

    @Test
    fun testShouldReturnArticlesAfterErrorForInitialLoad() {
        val testInitialErrorObservable = dataSource.initialErrorObservable.test()

        val mockArticlesResponse = mockk<ArticlesResponse.Error>()
        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)

        dataSource.loadInitial(params, callback)

        verify(exactly = 0) { callback.onResult(any(), any()) }

        testInitialErrorObservable.assertValue { it.response == mockArticlesResponse }.dispose()
    }

    @Test
    fun testShouldClearCachesAfterSuccessForInitialLoad() {
        val mockArticles =
            Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()

        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { mockArticlesResponse.data } returns mockArticles

        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)
        dataSource.loadInitial(params, callback)

        verify(atLeast = 20) { cacheDatabase.articles().insert(any()) }
        verify(atLeast = 20) { cacheDatabase.users().insert(any()) }
    }

    @Test
    fun testShouldCheckCachesForInitialLoad() {
        val mockArticles =
            Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()

        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { mockArticlesResponse.data } returns mockArticles

        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)
        dataSource.loadInitial(params, callback)

        verify { cacheDatabase.users().clear() }
        verify { cacheDatabase.comments().clear() }
        verify { cacheDatabase.articles().clear() }
        verify { imageDatabase.avatars().clear() }
    }

    @Test
    fun testShouldReturnArticlesForRangeLoad() {
        val mockArticles =
            Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()

        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { mockArticlesResponse.data } returns mockArticles

        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadRangeParams(20, 20)
        val callback = mockk<PositionalDataSource.LoadRangeCallback<Article>>(relaxed = true)

        dataSource.loadRange(params, callback)

        val slot = slot<List<Article>>()
        verify(exactly = 1) { callback.onResult(capture(slot)) }
        assertEquals(mockArticles, slot.captured)
    }

    @Test
    fun testShouldCheckCachesForRangeLoad() {
        val mockArticles =
            Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()

        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { mockArticlesResponse.data } returns mockArticles

        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadRangeParams(20, 20)
        val callback = mockk<PositionalDataSource.LoadRangeCallback<Article>>(relaxed = true)

        dataSource.loadRange(params, callback)

        verify(exactly = 20) { cacheDatabase.articles().insert(any()) }
        verify(exactly = 20) { cacheDatabase.users().insert(any()) }
    }

    @Test
    fun testShouldReturnArticlesAfterErrorForRangeLoad() {
        val testRangeErrorObservable = dataSource.rangeErrorObservable.test()

        val mockArticlesResponse = mockk<ArticlesResponse.Error>()
        every { manager.getArticles(any()) } returns Single.just(mockArticlesResponse)

        val params = PositionalDataSource.LoadRangeParams(20, 20)
        val callback = mockk<PositionalDataSource.LoadRangeCallback<Article>>(relaxed = true)

        dataSource.loadRange(params, callback)

        testRangeErrorObservable.assertValue { it.response == mockArticlesResponse }.dispose()
    }

    @Test
    fun testShouldLoadFromCacheAfterError() {
        val testInitialSuccessObservable = dataSource.initialSuccessObservable.test()

        val mockArticles =
            Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()
        every { cacheDatabase.articles().getAllSortedByDescendingTimePublished() } returns mockArticles
        every { manager.getArticles(any()) } returns Single.just(Unit)
            .map { throw RuntimeException(UnknownHostException()) }

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)

        dataSource.loadInitial(params, callback)

        testInitialSuccessObservable.assertValue { it.response.data == mockArticles }.dispose()
    }

    @Test(expected = RuntimeException::class)
    fun testShouldThrowNotNetworkException() {
        every { manager.getArticles(any()) } returns Single.just(Unit).map { throw RuntimeException(Exception()) }

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)
        dataSource.loadInitial(params, callback)
    }

    @Test
    fun testShouldReturnErrorIfCacheIsEmpty() {
        val testInitialErrorObservable = dataSource.initialErrorObservable.test()

        every { cacheDatabase.articles().getAllSortedByDescendingTimePublished() } returns emptyList()
        every { manager.getArticles(any()) } returns Single.just(Unit)
            .map { throw RuntimeException(UnknownHostException()) }

        val params = PositionalDataSource.LoadInitialParams(0, 0, 20, false)
        val callback = mockk<PositionalDataSource.LoadInitialCallback<Article>>(relaxed = true)

        dataSource.loadInitial(params, callback)

        testInitialErrorObservable.assertValueCount(1).dispose()
    }
}
