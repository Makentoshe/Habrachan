package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.model.post.*
import com.makentoshe.habrachan.model.post.html.*
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
    postId: Int,
    postRepository: Repository<Int, Single<Data>>,
    private val habrachanWebViewClient: HabrachanWebViewClient
) : ViewModel(), PostFragmentNavigationViewModel {

    private val disposables = CompositeDisposable()

    /** Emitter for publication data */
    private val publicationSubject = BehaviorSubject.create<String>()

    /** Observable for publication data */
    val publicationObservable: Observable<String>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    /** Error events needs to display error message */
    private val errorSubject = BehaviorSubject.create<Throwable>()

    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    /** Success events needs to display common content */
    private val successSubject = BehaviorSubject.create<Unit>()

    val successObservable: Observable<Unit>
        get() = successSubject

    init {
        postRepository.get(postId)!!.subscribe({ post ->
            val html = createHtml(post)
            publicationSubject.onNext(html)
            errorSubject.onComplete()
        }, {
            errorSubject.onNext(it)
            publicationSubject.onComplete()
        }).let(disposables::add)

        habrachanWebViewClient.onPublicationReadyToShow() {
            successSubject.onNext(Unit)
        }
    }

    private fun createHtml(post: Data): String {
        val builder = HtmlBuilder(post)
        builder.addAddon(DisplayScriptAddon(repository))
        builder.addAddon(StyleAddon(repository))
        builder.addAddon(TitleAddon(post))
        builder.addAddon(SpoilerAddon())
        return builder.build()
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
        private val postRepository: Repository<Int, Single<Data>>,
        private val habrachanWebViewClient: HabrachanWebViewClient
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(router, repository, postId, postRepository, habrachanWebViewClient) as T
        }
    }
}
