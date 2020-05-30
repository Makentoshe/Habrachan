package com.makentoshe.habrachan.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import com.google.android.material.button.MaterialButton
import com.makentoshe.habrachan.R
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class CommentsFlowFragmentUi {

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
        markupActionsContainer = fragmentView.findViewById(R.id.comments_flow_fragment_markup_container)
        sendButton = fragmentView.findViewById(R.id.comments_flow_fragment_send)
        messageEditText = fragmentView.findViewById(R.id.comments_flow_fragment_message_input)
        sendProgressBar = fragmentView.findViewById(R.id.comments_flow_fragment_send_progress)
        slidingPanel = fragmentView.findViewById(R.id.comments_flow_fragment_slidingpanel)
        return fragmentView
    }

    private fun internalInflateView(inflater: LayoutInflater, root: ViewGroup?): View {
        return inflater.inflate(R.layout.comments_flow_fragment, root, false)
    }
}