package com.makentoshe.habrachan.application.android.screen.comments.articles.view

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.makentoshe.habrachan.application.android.screen.comments.articles.R

class CommentsEmptyStateViewHolder(val root: View) {
    val title: TextView = root.findViewById(R.id.layout_empty_state_title)
    val message: TextView = root.findViewById(R.id.layout_empty_state_message)
    val button: Button = root.findViewById(R.id.layout_empty_state_button)
    val image: ImageView = root.findViewById(R.id.layout_empty_state_image)
}