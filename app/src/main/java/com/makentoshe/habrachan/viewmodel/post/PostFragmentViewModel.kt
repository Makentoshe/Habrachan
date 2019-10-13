package com.makentoshe.habrachan.viewmodel.post

import android.text.Spannable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.posts.Data
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class PostFragmentViewModel(
    private val page: Int,
    private val position: Int,
    private val requestFactory: GetPostsRequestFactory,
    private val cache: Cache<GetPostsRequest, PostsResponse>
) : ViewModel() {

    private val publicationSubject = BehaviorSubject.create<Data>()

    val publicationObservable: Observable<Data>
        get() = publicationSubject

    init {
        val request = requestFactory.stored(page + 1)
        val posts = cache.get(request)
        if (posts != null) {
            val post = posts.data[position]
            if (post.textHtml != null) {
                pushPublicationText(post)
            } else {
                pushPublicationPreview(post)
                startPublicationDownload()
            }
        } else {
            //todo add error observable
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
        private val requestFactory: GetPostsRequestFactory,
        private val cache: Cache<GetPostsRequest, PostsResponse>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, requestFactory, cache) as T
        }
    }
}