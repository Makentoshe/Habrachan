package com.makentoshe.habrachan.model.main.articles

import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.Article

class ArticlesEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory,
    private val articleDivideFactory: ArticleDivideEpoxyModel.Factory,
    private val articlePageDivideFactory: ArticlesPageDivideEpoxyModel.Factory,
    private val articlesSearchModelFactory: ArticlesSearchModel.Factory
) : EpoxyController() {

    private val articles = ArrayList<Article>()

    fun append(articles: List<Article>) {
        this.articles.addAll(articles)
    }

    val isEmpty: Boolean
        get() = articles.isEmpty()

    fun clear() = articles.clear()

    override fun buildModels() {
        buildArticleSearchModel()
        buildArticleModels()
    }

    private fun buildArticleModels() {
        var index = 0
        articles.chunked(20).forEachIndexed { page, posts ->
            if (page >= 1) {
                buildArticlePageDivide(index, page + 1)
                index += 1
            }
            buildArticlesPage(index, posts)
            index += posts.size * 2
        }
    }

    private fun buildArticlesPage(start: Int, posts: List<Article>) {
        var position = 0
        val end = start + posts.size * 2
        for (index in start until end step 2) {
            // add post model and divider
            buildArticle(index, posts[position])
            position += 1
            // avoid default divide for the last model in page
            // because there will be page divider instead
            if (index + 1 == end - 1) break
            buildArticleDivide(index + 1)
        }
    }

    private fun buildArticle(id: Int, article: Article) {
        articleModelFactory.build(id, article).addTo(this)
    }

    private fun buildArticleDivide(position: Int) {
        articleDivideFactory.build(position).addTo(this)
    }

    private fun buildArticlePageDivide(index: Int, page: Int) {
        articlePageDivideFactory.build(index, page).addTo(this)
    }

    private fun buildArticleSearchModel() {
        articlesSearchModelFactory.build().addTo(this)
    }
}

