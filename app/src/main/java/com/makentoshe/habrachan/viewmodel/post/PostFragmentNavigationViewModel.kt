package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.model.post.images.PostImagesScreen
import ru.terrakok.cicerone.Router

class PostFragmentNavigationViewModel(private val router: Router): ViewModel() {

    fun backToMainPostsScreen() {
        router.exit()
    }

    fun navigateToImagesScreen(index : Int, sources: Array<String>) {
        router.navigateTo(PostImagesScreen(index, sources))
    }

    class Factory(
        private val router: Router
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentNavigationViewModel(router) as T
        }
    }
}