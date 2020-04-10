package com.makentoshe.habrachan.viewmodel.main.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.model.main.articles.ArticleDivideEpoxyModel
import com.makentoshe.habrachan.model.main.articles.ArticleEpoxyModel
import com.makentoshe.habrachan.model.main.articles.ArticlesEpoxyController
import com.makentoshe.habrachan.model.main.articles.ArticlesPageDivideEpoxyModel

class ArticlesControllerViewModel(private val controller: ArticlesEpoxyController) : ViewModel() {

    val adapter: EpoxyControllerAdapter
        get() = controller.adapter

    fun clear() = controller.clear()

    fun appendAndRequestBuild(articles: List<Article>) {
        controller.append(articles)
        requestBuild()
    }

    fun requestBuild() = controller.requestModelBuild()

    fun shouldRequestBuild() = controller.isEmpty.not()

    class Factory(
        private val controller: ArticlesEpoxyController
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesControllerViewModel(controller) as T
        }
    }
}