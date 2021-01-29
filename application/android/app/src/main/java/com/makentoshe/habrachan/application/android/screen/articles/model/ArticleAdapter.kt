package com.makentoshe.habrachan.application.android.screen.articles.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticleViewHolder
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticleDiffUtilItemCallback
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticleModelElement
import com.makentoshe.habrachan.entity.timePublished
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(
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
        holder.time.text = model.article.timePublished.time(holder.context)
        holder.scoreCount.text = model.article.score.toString()
        holder.readingCount.text = model.article.readingCount.toString()
        holder.commentsCount.text = model.article.commentsCount.toString()
        holder.itemView.setOnClickListener {
            navigation.navigateToArticle(model.article)
        }
    }

    // todo move articles in controller
    private fun Date.time(context: Context): String {
        val date = SimpleDateFormat("dd MMMM yyyy").format(this)
        val time = SimpleDateFormat("HH:mm").format(this)
        return context.getString(R.string.articles_time_format, date, time)
    }
}