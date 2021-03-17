package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.recyclerview.widget.DiffUtil

class ArticleDiffUtilItemCallback : DiffUtil.ItemCallback<ArticleModelElement>() {

    override fun areItemsTheSame(oldItem: ArticleModelElement, newItem: ArticleModelElement): Boolean {
        return oldItem.article.articleId == newItem.article.articleId
    }

    override fun areContentsTheSame(oldItem: ArticleModelElement, newItem: ArticleModelElement): Boolean {
        return oldItem == newItem
    }
}