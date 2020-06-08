package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class SendCommentViewModelTest : BaseTest() {

    private lateinit var viewModel: SendCommentViewModel

    private val mockCommentsManager = mockk<CommentsManager>(relaxed = true)
    private val mockSessionDatabase = mockk<SessionDatabase>(relaxed = true)
    private val spyDisposables = spyk(CompositeDisposable())
    private val schedulerProvider = object : CommentsViewModelSchedulerProvider {
        override val networkScheduler = Schedulers.trampoline()
    }

    @Before
    fun before() {
        viewModel = SendCommentViewModel(schedulerProvider, spyDisposables, mockCommentsManager, mockSessionDatabase)
    }

    @Test
    @Ignore("Should be implement after view model")
    fun sas() {
        TODO("Should be implemented after view model")
    }

}