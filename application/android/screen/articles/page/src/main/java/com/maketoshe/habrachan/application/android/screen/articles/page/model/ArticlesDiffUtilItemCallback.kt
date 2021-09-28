package com.maketoshe.habrachan.application.android.screen.articles.page.model

import androidx.recyclerview.widget.DiffUtil
import com.makentoshe.habrachan.application.android.common.articles.model.ArticlesModelElement

class ArticlesDiffUtilItemCallback : DiffUtil.ItemCallback<ArticlesModelElement>() {

    override fun areItemsTheSame(oldItem: ArticlesModelElement, newItem: ArticlesModelElement): Boolean {
        return oldItem.article.articleId == newItem.article.articleId
    }

    override fun areContentsTheSame(oldItem: ArticlesModelElement, newItem: ArticlesModelElement): Boolean {
        return oldItem == newItem
    }
}