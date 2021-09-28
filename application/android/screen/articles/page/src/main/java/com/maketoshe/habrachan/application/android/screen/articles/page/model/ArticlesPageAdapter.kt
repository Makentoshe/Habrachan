package com.maketoshe.habrachan.application.android.screen.articles.page.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.model.ArticlesModelElement
import com.maketoshe.habrachan.application.android.screen.articles.page.R
import com.maketoshe.habrachan.application.android.screen.articles.page.view.ArticlesPageItemViewHolder
import com.maketoshe.habrachan.application.android.screen.articles.page.view.initialize
import com.maketoshe.habrachan.application.android.screen.articles.page.view.setOnClickListener
import javax.inject.Inject

class ArticlesPageAdapter @Inject constructor(
//    private val navigation: ArticlesNavigation
) : PagingDataAdapter<ArticlesModelElement, ArticlesPageItemViewHolder>(ArticlesDiffUtilItemCallback()) {

    companion object : Analytics(LogAnalytic())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesPageItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticlesPageItemViewHolder(inflater.inflate(R.layout.layout_articles_page_item, parent, false))
    }

    override fun onBindViewHolder(holder: ArticlesPageItemViewHolder, position: Int) {
        val model = getItem(position)
            ?: return capture(analyticEvent(throwable = NoSuchElementException("Comment is null at position $position")))

        holder.initialize(model.article)
        holder.setOnClickListener {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }
    }
}
