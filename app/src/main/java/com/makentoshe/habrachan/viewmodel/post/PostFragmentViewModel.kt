package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.model.post.PublicationRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostFragmentViewModel(position: Int, page: Int, publicationRepository: PublicationRepository) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<Data>()

    val publicationObservable: Observable<Data>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        publicationRepository.get(page, position).subscribe({ post ->
            val html = post.textHtml
            if (html != null) {
                publicationSubject.onNext(post)
            } else {
                // error
            }
        }, {

        }).let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val page: Int,
        private val position: Int,
        private val publicationRepository: PublicationRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, publicationRepository) as T
        }
    }
}

