package com.maketoshe.habrachan.application.android.screen.articles.page.view

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.application.android.common.time
import com.makentoshe.habrachan.entity.article.*
import com.makentoshe.habrachan.entity.article.author.authorLogin
import com.makentoshe.habrachan.entity.article.hub.title
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
    author.text = article.author.value.authorLogin.value.authorLogin
    hubs.text = article.hubs.value.joinToString(", ") { articleHub -> articleHub.title.value }
    title.text = article.articleTitle.value.articleTitle
    time.text = article.timePublished.value.timePublishedDate.time(context, R.string.articles_time_format)
    scoreCount.text = article.scoresCount.value.toString()
    readingCount.text = article.readingCount.value.toString()
    commentsCount.text = article.commentsCount.value.toString()
}

fun ArticlesPageItemViewHolder.setOnClickListener(action: (View) -> Unit) {
    itemView.setOnClickListener(action)
}