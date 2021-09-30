package com.maketoshe.habrachan.application.android.screen.articles.page.view

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.application.android.common.time
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.timePublished
import com.maketoshe.habrachan.application.android.screen.articles.page.R
import com.maketoshe.habrachan.application.android.screen.articles.page.databinding.LayoutArticlesPageItemBinding

class ArticlesPageItemViewHolder(binding: LayoutArticlesPageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = binding.root.context

    val title: TextView = binding.layoutArticlesPageItemTitle
    val author: TextView = binding.layoutArticlesPageItemAuthorLogin
    val time: TextView = binding.layoutArticlesPageItemTimePublished
    val hubs: TextView = binding.layoutArticlesPageItemHubsTitles
    val scoreCount: TextView = binding.layoutArticlesPageItemScoreTextview
    val readingCount: TextView = binding.layoutArticlesPageItemReadingCountTextview
    val commentsCount: TextView = binding.layoutArticlesPageItemCommentsCountTextview
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