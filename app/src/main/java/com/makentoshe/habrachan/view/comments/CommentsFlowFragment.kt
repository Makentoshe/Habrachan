package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.navigation.comments.CommentsScreenNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFlowFragment : Fragment() {

    private val disposables by inject<CompositeDisposable>()
    private val arguments by inject<CommentsScreenArguments>()
    private val navigator by inject<CommentsScreenNavigation>()
    private val commentsFlowFragmentUi by inject<CommentsFlowFragmentUi>()

    private val softKeyboardController = SoftKeyboardController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return commentsFlowFragmentUi.inflateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        softKeyboardController.addVisibilityChangeListener(requireActivity(), ::onSoftKeyboardVisibilityChanged)
        commentsFlowFragmentUi.sendButton.setOnClickListener { onMessageSend() }
        commentsFlowFragmentUi.messageEditText.doAfterTextChanged(::onMessageTextChanged)
    }

    private fun onMessageTextChanged(message: Editable?) {
        commentsFlowFragmentUi.sendButton.isEnabled = message?.isNotBlank() == true
    }

    private fun onSoftKeyboardVisibilityChanged(visible: Boolean) = if (visible) {
        commentsFlowFragmentUi.markupActionsContainer.visibility = View.VISIBLE
    } else {
        commentsFlowFragmentUi.messageEditText.clearFocus()
        if (commentsFlowFragmentUi.messageEditText.text.isBlank()) {
            commentsFlowFragmentUi.markupActionsContainer.visibility = View.GONE
        } else Unit
    }

    private fun onMessageSend() {
        val messageText = commentsFlowFragmentUi.messageEditText.text
        if (messageText.isBlank()) return
        softKeyboardController.hide(requireActivity())
        commentsFlowFragmentUi.messageEditText.setText("")
        commentsFlowFragmentUi.messageEditText.clearFocus()
        commentsFlowFragmentUi.markupActionsContainer.visibility = View.GONE
        //send message ->
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
    }

    class Factory {
        fun build(articleId: Int, article: Article?): CommentsFlowFragment {
            val fragment = CommentsFlowFragment()
            val arguments = CommentsScreenArguments(fragment)
            arguments.article = article
            arguments.articleId = articleId
            return fragment
        }
    }
}