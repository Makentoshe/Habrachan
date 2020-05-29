package com.makentoshe.habrachan.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.Group
import com.google.android.material.button.MaterialButton
import com.makentoshe.habrachan.R

class CommentsFlowFragmentUi {

    lateinit var markupActionsContainer: Group
        private set

    lateinit var sendButton: MaterialButton
        private set

    lateinit var messageEditText: EditText
        private set

    fun inflateView(inflater: LayoutInflater, root: ViewGroup? = null): View {
        val fragmentView = internalInflateView(inflater, root)
        markupActionsContainer = fragmentView.findViewById(R.id.comments_flow_fragment_markup)
        sendButton = fragmentView.findViewById(R.id.comments_flow_fragment_send)
        messageEditText = fragmentView.findViewById(R.id.comments_flow_fragment_message_input)
        return fragmentView
    }

    private fun internalInflateView(inflater: LayoutInflater, root: ViewGroup?): View {
        return inflater.inflate(R.layout.comments_flow_fragment, root, false)
    }
}