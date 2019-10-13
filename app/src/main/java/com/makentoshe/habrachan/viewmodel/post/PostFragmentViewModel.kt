package com.makentoshe.habrachan.viewmodel.post

import android.text.Spannable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PostFragmentViewModel(
    private val page: Int,
    private val position: Int,
    private val cache: Cache<Int, Data>,
    private val requestFactory: GetPostsRequestFactory,
    private val postsManager: HabrPostsManager
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<Data>()

    val publicationObservable: Observable<Data>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        val post = cache.get((page + 1) * 20 + position)
        if (post != null) {
            if (post.textHtml != null) {
                pushPublicationText(post)
            } else {
                pushPublicationPreview(post)
                startPublicationDownload(post.id)
            }
        } else {
            // error message wtf
        }
    }

    private fun pushPublicationText(post: Data) {
//        val text = post.textHtml ?: return //todo add error observable
        publicationSubject.onNext(post)
    }

    private fun pushPublicationPreview(post: Data) {
//        val preview = post.previewHtml
        publicationSubject.onNext(post)
    }

    private fun startPublicationDownload(id: Int) {
        val request = requestFactory.single(id)
        postsManager.getPost(request).subscribe({
            cache.set((page + 1) * 20 + position, it.data)
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
        private val postsManager: HabrPostsManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, cache, requestFactory, postsManager) as T
        }
    }
}
