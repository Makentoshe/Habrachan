package com.makentoshe.habrachan.viewmodel.post.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/* ViewModel for downloading image in view mode for PostImageFragmentPage */
class PostImageFragmentViewModel : ViewModel() {

    private fun loadImage(imageSource: String) {
        println(imageSource)
    }

    class Factory(private val imageSource: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostImageFragmentViewModel().apply { loadImage(imageSource) } as T
        }
    }
}