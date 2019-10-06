package com.makentoshe.habrachan.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostsPageViewModel(private val position: Int) : ViewModel() {

    init {
        println("Start content loading for page $position")
    }

    class Factory(private val position: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsPageViewModel(position) as T
        }
    }
}