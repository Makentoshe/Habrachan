package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.model.post.PublicationRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import java.io.InputStream

class PostFragmentViewModel(
    position: Int, page: Int, publicationRepository: PublicationRepository,
    private val router: Router,
    private val rawResourceRepository: Repository<Int, InputStream>
) : ViewModel(), PostFragmentNavigationViewModel {

    private val disposables = CompositeDisposable()

    private val publicationSubject = BehaviorSubject.create<String>()

    val publicationObservable: Observable<String>
        get() = publicationSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        publicationRepository.get(page, position).subscribe({ post ->
            publicationSubject.onNext(processHtml(post))
        }, {

        }).let(disposables::add)
    }

    private fun processHtml(post: Data): String {
        val html = post.textHtml
        if (html != null) {
            val styleHtml = StyleHtmlBuilder(rawResourceRepository).build()
            val titleHtml = TitleHtmlBuilder(post.title).build()
            return StringBuilder().append(styleHtml).append(titleHtml).append(html).toString()
        } else {
            //err
            return ""
        }
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
        private val publicationRepository: PublicationRepository,
        private val router: Router,
        private val rawResourceRepository: Repository<Int, InputStream>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, publicationRepository, router, rawResourceRepository) as T
        }
    }
}

class TitleHtmlBuilder(private val title: String) {
    fun build(): CharSequence {
        return StringBuilder().append("<h1>").append(title).append("</h1>")
    }
}

class StyleHtmlBuilder(private val rawResourceRepository: Repository<Int, InputStream>) {

    fun build(): CharSequence {
        val cssBytes = rawResourceRepository.get(com.makentoshe.habrachan.R.raw.post)?.readBytes()
        val cssStyle = if (cssBytes != null) String(cssBytes) else ""
        return StringBuilder("<style>").append(cssStyle).append("</style>")
    }
}

