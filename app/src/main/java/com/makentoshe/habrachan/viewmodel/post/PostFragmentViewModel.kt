package com.makentoshe.habrachan.viewmodel.post

import android.text.Spanned
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.model.post.PublicationParser
import com.makentoshe.habrachan.model.post.PublicationRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostFragmentViewModel(
    private val position: Int,
    private val page: Int,
    private val publicationParser: PublicationParser,
    private val publicationRepository: PublicationRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<Spanned>()

    val publicationObservable: Observable<Spanned>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        publicationRepository.get(page, position).subscribe({ post ->
            val html = post.textHtml
            if (html != null) {
                pushPublicationText(post)
            } else {
                // error
            }
        }, {

        }).let(disposables::add)
    }

    private fun pushPublicationText(post: Data) {
        post.textHtml ?: return //todo add error observable
        val spanned = publicationParser.parse(post.textHtml)
        publicationSubject.onNext(spanned)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val page: Int,
        private val position: Int,
        private val publicationParser: PublicationParser,
        private val publicationRepository: PublicationRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, publicationParser, publicationRepository) as T
        }
    }
}

