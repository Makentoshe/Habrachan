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

class CommentsReplyFragmentUi {

    lateinit var toolbar: Toolbar
        private set

    lateinit var markupActionsContainer: View
        private set

    lateinit var sendButton: MaterialButton
        private set

    lateinit var sendProgressBar: ProgressBar
        private set

    lateinit var messageEditText: EditText
        private set

    lateinit var slidingPanel: SlidingUpPanelLayout
        private set

    fun inflateView(inflater: LayoutInflater, root: ViewGroup? = null): View {
        val fragmentView = internalInflateView(inflater, root)
        toolbar = fragmentView.findViewById(R.id.comments_reply_toolbar)
        markupActionsContainer = fragmentView.findViewById(R.id.comments_reply_markup)
        sendButton = fragmentView.findViewById(R.id.comments_markup_send)
        messageEditText = fragmentView.findViewById(R.id.comments_reply_message_input)
        sendProgressBar = fragmentView.findViewById(R.id.comments_markup_send_progress)
        slidingPanel = fragmentView.findViewById(R.id.comments_reply_sliding)
        return fragmentView
    }

    private fun internalInflateView(inflater: LayoutInflater, root: ViewGroup?): View {
        return inflater.inflate(R.layout.comments_reply_fragment, root, false)
    }
}