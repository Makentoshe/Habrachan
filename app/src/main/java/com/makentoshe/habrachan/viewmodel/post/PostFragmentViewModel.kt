package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.model.post.GetArticle
import com.makentoshe.habrachan.model.post.VoteArticle
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.InputStream

class PostFragmentViewModel(
    repository: Repository<Int, InputStream>,
    postRepository: Repository<Int, Single<Article>>,
    postId: Int
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val voteArticle = VoteArticle(disposables, postId)
    val getArticle = GetArticle(disposables, repository, postRepository, postId)

    init {
        getArticle.requestArticle()
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val repository: Repository<Int, InputStream>,
        private val postRepository: Repository<Int, Single<Article>>,
        private val postId: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(repository, postRepository, postId) as T
        }
    }
}
