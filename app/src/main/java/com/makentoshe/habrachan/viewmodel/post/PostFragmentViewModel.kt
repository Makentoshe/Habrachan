package com.makentoshe.habrachan.viewmodel.post

import android.text.Spannable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.posts.Data
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class PostFragmentViewModel(
    private val page: Int,
    private val position: Int,
    private val cache: Cache<Int, Data>
) : ViewModel() {

    private val publicationSubject = BehaviorSubject.create<Data>()

    val publicationObservable: Observable<Data>
        get() = publicationSubject

    init {
        val post = cache.get((page + 1) + position)
        if (post == null) {
            // load post with body
        } else {
            if (post.textHtml != null) {
                // show post body
            } else {
                // load post's body
            }
        }
    }

    private fun pushPublicationText(post: Data) {
        val text = post.textHtml ?: return //todo add error observable
        parse(text)
        publicationSubject.onNext(post)
    }

    private fun pushPublicationPreview(post: Data) {
        val preview = post.previewHtml
        parse(preview)
        publicationSubject.onNext(post)
    }

    private fun startPublicationDownload() {
    }

    private fun parse(html: String) {
        val sas = Spannable.Factory().newSpannable(html)
        println(sas)
    }

    class Factory(
        private val page: Int,
        private val position: Int,
        private val cache: Cache<Int, Data>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, cache) as T
        }
    }
}