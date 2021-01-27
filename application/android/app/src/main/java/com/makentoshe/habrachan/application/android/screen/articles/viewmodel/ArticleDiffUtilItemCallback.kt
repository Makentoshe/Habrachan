package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.recyclerview.widget.DiffUtil

class ArticleDiffUtilItemCallback : DiffUtil.ItemCallback<ArticleModelElement>() {

    override fun areItemsTheSame(oldItem: ArticleModelElement, newItem: ArticleModelElement): Boolean {
        return oldItem.article.id == newItem.article.id
    }

    override fun areContentsTheSame(oldItem: ArticleModelElement, newItem: ArticleModelElement): Boolean {
        return oldItem == newItem
    }
}