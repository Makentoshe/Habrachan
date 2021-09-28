package com.maketoshe.habrachan.application.android.screen.articles.page.view

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.application.android.common.time
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.timePublished
import com.maketoshe.habrachan.application.android.screen.articles.page.R

class ArticlesPageItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = itemView.context
    val title: TextView = itemView.findViewById(R.id.title)
    val author: TextView = itemView.findViewById(R.id.author_login)
    val time: TextView = itemView.findViewById(R.id.time_published)
    val hubs: TextView = itemView.findViewById(R.id.hubs_titles)
    val scoreCount: TextView = itemView.findViewById(R.id.score_textview)
    val readingCount: TextView = itemView.findViewById(R.id.reading_count_textview)
    val commentsCount: TextView = itemView.findViewById(R.id.comments_count_textview)
}

fun ArticlesPageItemViewHolder.initialize(article: Article) {
    author.text = article.author.login
    hubs.text = article.hubs.joinToString(", ") { it.title }
    title.text = article.title
    time.text = article.timePublished.time(context, R.string.articles_time_format)
    scoreCount.text = article.score.toString()
    readingCount.text = article.readingCount.toString()
    commentsCount.text = article.commentsCount.toString()
}

fun ArticlesPageItemViewHolder.setOnClickListener(action: (View) -> Unit) {
   itemView.setOnClickListener(action)
}