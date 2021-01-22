package com.makentoshe.habrachan.application.android.screen.articles.view

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = itemView.context
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author_login)
    val time: TextView = itemView.findViewById(R.id.time_published)
    val hubs: TextView = itemView.findViewById(R.id.hubs_titles)
    val scoreCount: TextView = itemView.findViewById(R.id.score_textview)
    val readingCount: TextView = itemView.findViewById(R.id.reading_count_textview)
    val commentsCount: TextView = itemView.findViewById(R.id.comments_count_textview)
}