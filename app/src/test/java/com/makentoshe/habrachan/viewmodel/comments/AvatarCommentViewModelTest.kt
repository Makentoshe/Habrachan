package com.makentoshe.habrachan.viewmodel.comments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.response.ImageResponse
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("ReactiveStreamsUnusedPublisher")
class AvatarCommentViewModelTest : BaseTest() {

    private lateinit var viewModel: AvatarCommentViewModel

    private val mockCommentsManager = mockk<CommentsManager>(relaxed = true)
    private val mockImageManager = mockk<ImageManager>(relaxed = true)
    private val mockCacheDatabase = mockk<CacheDatabase>(relaxed = true)
    private val mockSessionDatabase = mockk<SessionDatabase>(relaxed = true)
    private val schedulerProvider = object : CommentsViewModelSchedulerProvider {
        override val networkScheduler = Schedulers.trampoline()
    }

    @Before
    fun before() {
        viewModel = AvatarCommentViewModel.Factory(mockImageManager, mockCacheDatabase).buildViewModel(mockk(relaxed = true))
    }

    @Test
    fun testShouldReturnStubAvatarWithoutNetworkAndCache() {
        val response = viewModel.getAvatarObservable("stub-user").blockingFirst()
        assert((response as ImageResponse.Success).isStub)

        verify(exactly = 0) { mockImageManager.getImage(any()) }
        verify(exactly = 0) { mockCacheDatabase.avatars().get(any()) }
    }

    @Test
    fun testShouldReturnSuccessImageResponseFromCache() {
        val mockBitmap = mockk<Bitmap>(relaxed = true)
        every { mockCacheDatabase.avatars().get(any()) } returns mockBitmap

        val response = viewModel.getAvatarObservable("from cache").blockingFirst()
        assert(response is ImageResponse.Success)

        verify { mockCacheDatabase.avatars().get(any()) }
        verify(exactly = 0) { mockImageManager.getImage(any()) }
    }

    @Test
    fun testShouldReturnSuccessImageResponseOnAvatarRequest() {
        mockkStatic(BitmapFactory::class)
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns mockk(relaxed = true)

        every { mockCacheDatabase.avatars().get(any()) } returns null

        val mockImageResponse = mockk<ImageResponse.Success>(relaxed = true)
        every { mockImageManager.getImage(any()) } returns Single.just(mockImageResponse)

        val response = viewModel.getAvatarObservable("from network").blockingFirst()
        Assert.assertEquals(response, mockImageResponse)

        unmockkStatic(BitmapFactory::class)
    }

    @Test
    fun testShouldSaveSuccessImageResponseToCache() {
        mockkStatic(BitmapFactory::class)
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns mockk(relaxed = true)
        every { mockImageManager.getImage(any()) } returns Single.just(mockk<ImageResponse.Success>(relaxed = true))
        every { mockCacheDatabase.avatars().get("from network") } returns null

        viewModel.getAvatarObservable("from network").blockingFirst()

        verify { mockCacheDatabase.avatars().insert("from network", any()) }

        unmockkStatic(BitmapFactory::class)
    }

    @Test
    fun testShouldReturnErrorImageResponseFromNetwork() {
        every { mockImageManager.getImage(any()) } returns Single.just(Exception("exception")).map { throw it }
        every { mockCacheDatabase.avatars().get("from network") } returns null

        val response = viewModel.getAvatarObservable("from network").blockingFirst()
        assert(response is ImageResponse.Error)
    }

    @Test
    fun testShouldReturnErrorImageResponseOnNetworkError() {
        every { mockCacheDatabase.avatars().get(any()) } throws Exception("something wrong")

        val response = viewModel.getAvatarObservable("").blockingFirst()
        assert(response is ImageResponse.Error)
    }
}