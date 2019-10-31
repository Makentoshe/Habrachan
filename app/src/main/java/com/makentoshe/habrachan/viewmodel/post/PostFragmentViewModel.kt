package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.model.post.BaseHtmlBuilder
import com.makentoshe.habrachan.model.post.PublicationRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.io.InputStream

class PostFragmentViewModel(
    position: Int, page: Int,
    publicationRepository: Repository<Int, Single<Data>>,
    private val router: Router,
    private val rawResourceRepository: Repository<Int, InputStream>,
    private val postId: Int
) : ViewModel(), PostFragmentNavigationViewModel {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<String>()

    val publicationObservable: Observable<String>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        publicationRepository.get(page * 20 + position)?.subscribe({ post ->
            val html = BaseHtmlBuilder(post, rawResourceRepository).build()
            publicationSubject.onNext(html)
        }, {

        })?.let(disposables::add)
    }

    override fun backToMainPostsScreen() {
        router.exit()
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val page: Int,
        private val position: Int,
        private val publicationRepository: Repository<Int, Single<Data>>,
        private val router: Router,
        private val rawResourceRepository: Repository<Int, InputStream>,
        private val postId: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, publicationRepository, router, rawResourceRepository, postId) as T
        }
    }
}
