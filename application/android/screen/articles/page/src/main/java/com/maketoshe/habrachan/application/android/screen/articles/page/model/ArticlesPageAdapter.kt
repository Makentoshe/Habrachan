package com.maketoshe.habrachan.application.android.screen.articles.page.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.model.ArticleModel
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ArticleScreenNavigator
import com.makentoshe.habrachan.entity.article.articleId
import com.makentoshe.habrachan.entity.articleId
import com.maketoshe.habrachan.application.android.screen.articles.page.databinding.LayoutArticlesPageItemBinding
import com.maketoshe.habrachan.application.android.screen.articles.page.view.ArticlesPageItemViewHolder
import com.maketoshe.habrachan.application.android.screen.articles.page.view.initialize
import com.maketoshe.habrachan.application.android.screen.articles.page.view.setOnClickListener
import javax.inject.Inject

class ArticlesPageAdapter @Inject constructor(
    private val articleScreenNavigator: ArticleScreenNavigator,
) : PagingDataAdapter<ArticleModel, ArticlesPageItemViewHolder>(ArticlesDiffUtilItemCallback()) {

    companion object : Analytics(LogAnalytic())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesPageItemViewHolder {
        return ArticlesPageItemViewHolder(LayoutArticlesPageItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ArticlesPageItemViewHolder, position: Int) = try {
        val model = getItem(position)
        if (model == null) {
            capture(analyticEvent(throwable = NoSuchElementException("Comment is null at position $position")))
        } else {
            holder.initialize(model.article)
            holder.setOnClickListener {
                val newArticleId = model.article.articleId.value
                articleScreenNavigator.toArticleScreen(articleId(newArticleId.articleId))
            }
        }
    } catch (exception: Exception) {
        capture(analyticEvent(throwable = exception))
    }
}
