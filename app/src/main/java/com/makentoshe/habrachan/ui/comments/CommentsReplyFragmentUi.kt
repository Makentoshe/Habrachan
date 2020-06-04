package com.makentoshe.habrachan.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.makentoshe.habrachan.R
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class CommentsReplyFragmentUi : CommentsInputFragmentUi {

    lateinit var toolbar: Toolbar
        private set

    private lateinit var internalMarkupActionsContainer: View
    override val markupActionsContainer: View
        get() = internalMarkupActionsContainer

    private lateinit var internalSendButton: MaterialButton
    override val sendButton: MaterialButton
        get() = internalSendButton

    private lateinit var internalSendProgressBar: ProgressBar
    override val sendProgressBar: ProgressBar
        get() = internalSendProgressBar

    private lateinit var internalMessageEditText: EditText
    override val messageEditText: EditText
        get() = internalMessageEditText

    private lateinit var internalSlidingPanel: SlidingUpPanelLayout
    override val slidingPanel: SlidingUpPanelLayout
        get() = internalSlidingPanel

    fun inflateView(inflater: LayoutInflater, root: ViewGroup? = null): View {
        val fragmentView = internalInflateView(inflater, root)
        toolbar = fragmentView.findViewById(R.id.comments_reply_toolbar)
        internalMarkupActionsContainer = fragmentView.findViewById(R.id.comments_reply_markup)
        internalSendButton = fragmentView.findViewById(R.id.comments_markup_send)
        internalMessageEditText = fragmentView.findViewById(R.id.comments_reply_message_input)
        internalSendProgressBar = fragmentView.findViewById(R.id.comments_markup_send_progress)
        internalSlidingPanel = fragmentView.findViewById(R.id.comments_reply_sliding)
        return fragmentView
    }

    private fun internalInflateView(inflater: LayoutInflater, root: ViewGroup?): View {
        return inflater.inflate(R.layout.comments_reply_fragment, root, false)
    }
}