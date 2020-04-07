package com.makentoshe.habrachan.viewmodel.main.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.main.articles.PostModelFactory
import com.makentoshe.habrachan.model.main.articles.PostsEpoxyController
import com.makentoshe.habrachan.common.navigation.Router

class ArticlesControllerViewModel(private val controller: PostsEpoxyController) : ViewModel() {

    val adapter: EpoxyControllerAdapter
        get() = controller.adapter

    fun clear() = controller.clear()

    fun appendAndRequestBuild(articles: List<Article>) {
        controller.append(articles)
        requestBuild()
    }

    fun requestBuild() = controller.requestModelBuild()

    fun shouldRequestBuild() = controller.isEmpty.not()

    class Factory(private val router: Router) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val controller = PostsEpoxyController(PostModelFactory(router))
            return ArticlesControllerViewModel(controller) as T
        }
    }
}