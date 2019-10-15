package com.makentoshe.habrachan.viewmodel.post

import android.text.Spanned
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.model.post.PublicationParser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostFragmentViewModel(
    private val position: Int,
    private val page: Int,
    private val cache: Cache<Int, Data>,
    private val requestFactory: GetPostsRequestFactory,
    private val postsManager: HabrPostsManager,
    private val publicationParser: PublicationParser
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<Spanned>()

    val publicationObservable: Observable<Spanned>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        val int = page * 20 + position
        val post = cache.get(int)
        if (post != null) {
            if (post.textHtml != null) {
                pushPublicationText(post)
            } else {
//                pushPublicationPreview(post)
                startPublicationDownload(post.id)
            }
        } else {
            println("error sas")
            // error message wtf
        }
    }

    private fun pushPublicationText(post: Data) {
        post.textHtml ?: return //todo add error observable
        val spanned = publicationParser.parse(post.textHtml)
        publicationSubject.onNext(spanned)
    }

    private fun startPublicationDownload(id: Int) {
        val request = requestFactory.single(id)
        postsManager.getPost(request).subscribe({
            cache.set(page * 20 + position, it.data)
            pushPublicationText(it.data)
        }, {
            // error
            println("sas")
        }).let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val page: Int,
        private val position: Int,
        private val cache: Cache<Int, Data>,
        private val requestFactory: GetPostsRequestFactory,
        private val postsManager: HabrPostsManager,
        private val publicationParser: PublicationParser
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, cache, requestFactory, postsManager, publicationParser) as T
        }
    }
}

