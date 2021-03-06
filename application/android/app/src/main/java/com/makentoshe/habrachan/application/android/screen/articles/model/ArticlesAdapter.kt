package com.makentoshe.habrachan.application.android.screen.articles.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticleViewHolder
import com.makentoshe.habrachan.application.android.time
import com.makentoshe.habrachan.entity.timePublished

class ArticlesAdapter(
    private val navigation: ArticlesNavigation
) : PagingDataAdapter<ArticleModelElement, ArticleViewHolder>(ArticleDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "ArticleAdapter", message())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleViewHolder(inflater.inflate(R.layout.layout_article_item, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(Log.ERROR) {
            "Comment is null at position $position"
        }

        holder.author.text = model.article.author.login
        holder.hubs.text = model.article.hubs.joinToString(", ") { it.title }
        holder.title.text = model.article.title
        holder.time.text = model.article.timePublished.time(holder.context, R.string.articles_time_format)
        holder.scoreCount.text = model.article.score.toString()
        holder.readingCount.text = model.article.readingCount.toString()
        holder.commentsCount.text = model.article.commentsCount.toString()
        holder.itemView.setOnClickListener {
            navigation.navigateToArticle(model.article)
        }
    }
}