package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.model.comments.SendCommentData
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class CommentsInputFragment : Fragment() {

    abstract val commentsInputFragmentUi: CommentsInputFragmentUi
    abstract val sendCommentViewModel: SendCommentViewModel
    abstract val disposables: CompositeDisposable
    abstract val softKeyboardController: SoftKeyboardController

    open val replyToParentId: Int
        get() = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        softKeyboardController.addVisibilityChangeListener(requireActivity(), ::onSoftKeyboardVisibilityChanged)
        commentsInputFragmentUi.sendButton.setOnClickListener { onMessageSend() }
        commentsInputFragmentUi.messageEditText.doAfterTextChanged(::onMessageTextChanged)

        sendCommentViewModel.sendCommentResponseObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            commentsInputFragmentUi.sendButton.visibility = View.VISIBLE
            commentsInputFragmentUi.sendProgressBar.visibility = View.GONE
            commentsInputFragmentUi.messageEditText.setText("")
            commentsInputFragmentUi.slidingPanel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }.let(disposables::add)
    }

    private fun onMessageTextChanged(message: Editable?) {
        commentsInputFragmentUi.sendButton.isEnabled = message?.isNotBlank() == true
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
    }

    private fun onSoftKeyboardVisibilityChanged(visible: Boolean) = if (visible) {
        commentsInputFragmentUi.markupActionsContainer.visibility = View.VISIBLE
        commentsInputFragmentUi.sendButton.visibility = View.VISIBLE
    } else {
        commentsInputFragmentUi.messageEditText.clearFocus()
        if (commentsInputFragmentUi.messageEditText.text.isBlank()) {
            commentsInputFragmentUi.markupActionsContainer.visibility = View.GONE
            commentsInputFragmentUi.sendButton.visibility = View.GONE
        } else Unit
    }

    private fun onMessageSend() {
        val messageText = commentsInputFragmentUi.messageEditText.text
        if (messageText.isBlank()) return
        commentsInputFragmentUi.sendButton.visibility = View.GONE
        commentsInputFragmentUi.sendProgressBar.visibility = View.VISIBLE

        val sendCommentData = SendCommentData(messageText, replyToParentId)
        sendCommentViewModel.sendCommentRequestObserver.onNext(sendCommentData)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}