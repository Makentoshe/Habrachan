package com.makentoshe.habrachan.view.comments

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.BaseRobolectricTest
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.model.comments.SendCommentData
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.*
import org.junit.Test
import org.robolectric.android.controller.ActivityController

abstract class CommentsInputFragmentTest : BaseRobolectricTest() {

    abstract fun buildActivityController(): ActivityController<AppActivity>
    abstract val mockSendCommentViewModel: SendCommentViewModel
    abstract val spyDisposables: CompositeDisposable
    abstract val mockSoftKeyboardController: SoftKeyboardController

    @Test
    fun testShouldInvokeSendCommentViewModelOnSendButtonClick() {
        val messageText = "Sas"

        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)

        messageView.setText(messageText)
        sendButton.performClick()

        val slot = slot<SendCommentData>()
        verify { mockSendCommentViewModel.sendCommentRequestObserver.onNext(capture(slot)) }

        assertEquals(messageText, slot.captured.message.toString())
    }

    @Test
    fun testShouldClearDisposables() {
        val activityController = buildActivityController()
        activityController.destroy()
        verify { spyDisposables.clear() }
    }

    @Test
    fun testShouldEnableSendButton() {
        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)

        assertFalse(sendButton.isEnabled)
        messageView.setText("  \n \t ")
        assertFalse(sendButton.isEnabled)
        messageView.setText("not blank message")
        assertTrue(sendButton.isEnabled)
    }

    @Test
    fun testShouldDisplayProgressBarOnSendButtonClick() {
        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)
        val sendButtonProgress = activity.findViewById<ProgressBar>(R.id.comments_markup_send_progress)

        messageView.setText("sas")
        sendButton.performClick()

        assertEquals(View.VISIBLE, sendButtonProgress.visibility)
        assertEquals(View.GONE, sendButton.visibility)
    }

    @Test
    fun testShouldHideProgressBarOnSendActionComplete() {
        val mockObservable = BehaviorSubject.create<CharSequence>()
        every { mockSendCommentViewModel.sendCommentResponseObservable } returns mockObservable

        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)
        val sendButtonProgress = activity.findViewById<ProgressBar>(R.id.comments_markup_send_progress)

        messageView.setText("sas")
        sendButton.performClick()

        mockObservable.onNext("CharSequence")

        assertEquals(View.GONE, sendButtonProgress.visibility)
    }

    @Test
    fun testShouldShowDisabledSendButtonOnSendActionComplete() {
        val mockObservable = BehaviorSubject.create<CharSequence>()
        every { mockSendCommentViewModel.sendCommentResponseObservable } returns mockObservable

        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)

        messageView.setText("sas")
        sendButton.performClick()
        mockObservable.onNext("CharSequence")

        assertEquals(View.VISIBLE, sendButton.visibility)
        assertFalse(sendButton.isEnabled)
    }

    @Test
    fun testShouldClearMessageEditTextOnSendActionComplete() {
        val mockObservable = BehaviorSubject.create<CharSequence>()
        every { mockSendCommentViewModel.sendCommentResponseObservable } returns mockObservable

        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)

        messageView.setText("sas")
        sendButton.performClick()
        mockObservable.onNext("CharSequence")

        assertTrue(messageView.text.isEmpty())
    }

    @Test
    fun testShouldCollapseMessagePanelOnSendActionComplete() {
        val mockObservable = BehaviorSubject.create<CharSequence>()
        every { mockSendCommentViewModel.sendCommentResponseObservable } returns mockObservable

        val activity = buildActivityController().get()
        val messageView = activity.findViewById<EditText>(R.id.comments_flow_fragment_message_input)
        val sendButton = activity.findViewById<Button>(R.id.comments_markup_send)
        val messagePanel = activity.findViewById<SlidingUpPanelLayout>(R.id.comments_flow_fragment_sliding)

        messageView.setText("sas")
        sendButton.performClick()
        mockObservable.onNext("CharSequence")

        assertEquals(SlidingUpPanelLayout.PanelState.COLLAPSED, messagePanel.panelState)
    }

}