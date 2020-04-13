package com.makentoshe.habrachan.viewmodel.main.articles

import android.os.Looper
import androidx.paging.PagedList
import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesDataSource
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesPagedListEpoxyController
import io.mockk.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor

class ArticlesViewModelTest : BaseTest() {

    init {
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk(relaxed = true)
    }

    private lateinit var viewModel: ArticlesViewModel
    private val controller = mockk<ArticlesPagedListEpoxyController>(relaxed = true)
    private val dataSource = mockk<ArticlesDataSource>(relaxed = true)
    private val executorsProvider = object : ArticlesViewModelExecutorsProvider {
        override val fetchExecutor = Executor { it.run() }
        override val notifyExecutor = Executor { it.run() }
    }
    private val schedulersProvider = object : ArticlesViewModelSchedulersProvider {
        override val ioScheduler = Schedulers.trampoline()
    }

    @Before
    fun before() {
        every { controller.pageSize } returns 20
    }

    @Test
    fun testShouldSubmitNewListOnRequest() {
        every { dataSource.initialSuccessObservable } returns PublishSubject.create()
        every { dataSource.initialErrorObservable } returns PublishSubject.create()
        every { dataSource.rangeErrorObservable } returns PublishSubject.create()
        viewModel = ArticlesViewModel(dataSource, controller, executorsProvider, schedulersProvider)

        every { controller.submitList(any()) } just runs

        viewModel.requestObserver.onNext(Unit)

        val slot = slot<PagedList<Article>>()
        verify(exactly = 1) { controller.submitList(capture(slot)) }
        val config = slot.captured.config
        assertEquals(20, config.pageSize)
        assertEquals(0, config.initialLoadSizeHint)
    }

}