package com.makentoshe.habrachan.view.comments

import android.content.Intent
import android.widget.EditText
import com.google.gson.Gson
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.comments.CommentsReplyScreen
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

@RunWith(RobolectricTestRunner::class)
class CommentsReplyFragmentTest : CommentsInputFragmentTest() {

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    override val spyDisposables = spyk(CompositeDisposable())
    override val mockSoftKeyboardController: SoftKeyboardController = spyk(SoftKeyboardController())
    override val mockSendCommentViewModel = mockk<SendCommentViewModel>(relaxed = true)

    private val router by inject<Router>()

    override fun buildActivityController(): ActivityController<AppActivity> {
        activityController.setup().get()
        val json = getJsonResponse("get_comments_success.json")
        val response = Gson().fromJson(json, GetCommentsResponse.Success::class.java)
        router.navigateTo(CommentsReplyScreen(response.data))
        return activityController
    }

    override fun getMessageView(activity: AppActivity): EditText {
        return activity.findViewById(R.id.comments_reply_message_input)
    }

    override fun getPanelView(activity: AppActivity): SlidingUpPanelLayout {
        return activity.findViewById(R.id.comments_reply_sliding)
    }

    @Before
    fun before() {
        Toothpick.openScopes(
            ApplicationScope::class.java, CommentsReplyFragment::class.java
        ).installTestModules(module {
            bind<CompositeDisposable>().toInstance(spyDisposables)
            bind<SendCommentViewModel>().toInstance(mockSendCommentViewModel)
        }).inject(this)
    }

    @After
    fun after() {
        Toothpick.closeScope(CommentsReplyFragment::class.java)
    }

}