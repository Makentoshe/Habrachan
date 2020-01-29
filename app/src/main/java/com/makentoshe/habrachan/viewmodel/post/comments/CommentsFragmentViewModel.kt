package com.makentoshe.habrachan.viewmodel.post.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommentsFragmentViewModel(private val articleId: Int) : ViewModel() {

    init {
        // start load using article id
        println(articleId)
    }

    class Factory(private val articleId: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CommentsFragmentViewModel(articleId) as T
        }
    }
}