package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.terrakok.cicerone.Router
import java.util.*

class PostFragmentNavigationViewModel(private val router: Router): ViewModel() {

    fun backToMainPostsScreen() {
        router.exit()
    }

    fun navigateToImagesScreen(index : Int, sources: Array<String>) {
        println(index)
        println(Arrays.toString(sources))
    }

    class Factory(
        private val router: Router
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentNavigationViewModel(router) as T
        }
    }
}