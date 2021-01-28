package com.makentoshe.habrachan.application.android.screen.comments.view

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.makentoshe.habrachan.R

class CommentsEmptyStateViewHolder(val root: View) {
    val title: TextView = root.findViewById(R.id.layout_empty_state_title)
    val message: TextView = root.findViewById(R.id.layout_empty_state_message)
    val button: Button = root.findViewById(R.id.layout_empty_state_button)
}