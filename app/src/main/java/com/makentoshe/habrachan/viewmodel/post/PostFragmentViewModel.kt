package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.model.post.BaseHtmlBuilder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.io.InputStream

class PostFragmentViewModel(
    private val router: Router,
    private val repository: Repository<Int, InputStream>,
    postId: Int, postRepository: Repository<Int, Single<Data>>
) : ViewModel(), PostFragmentNavigationViewModel {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<String>()

    val publicationObservable: Observable<String>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        postRepository.get(postId)!!.subscribe({ post ->
            val html = BaseHtmlBuilder(post, repository).build()
            publicationSubject.onNext(html)
        }, {
            it.printStackTrace()
        }).let(disposables::add)
    }

    override fun backToMainPostsScreen() {
        router.exit()
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val router: Router,
        private val repository: Repository<Int, InputStream>,
        private val postId: Int,
        private val postRepository: Repository<Int, Single<Data>>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(router, repository, postId, postRepository) as T
        }
    }
}
