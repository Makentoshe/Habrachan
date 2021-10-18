package com.maketoshe.habrachan.application.android.screen.articles.page.model

import androidx.recyclerview.widget.DiffUtil
import com.makentoshe.habrachan.application.android.common.articles.model.ArticleModel
import com.makentoshe.habrachan.entity.article.articleId

class ArticlesDiffUtilItemCallback : DiffUtil.ItemCallback<ArticleModel>() {

    override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.article.articleId == newItem.article.articleId
    }

    override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem == newItem
    }
}