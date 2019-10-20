package com.makentoshe.habrachan.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.model.post.PublicationRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router

class PostFragmentViewModel(
    position: Int, page: Int, publicationRepository: PublicationRepository, private val router: Router
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
            val styleHtml = StyleHtmlBuilder().build()
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
        private val router: Router
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostFragmentViewModel(page, position, publicationRepository, router) as T
        }
    }
}

class TitleHtmlBuilder(private val title: String) {
    fun build(): CharSequence {
        return StringBuilder().append("<h1>").append(title).append("</h1>")
    }
}

class StyleHtmlBuilder {

    fun build(): CharSequence {
        val builder = StringBuilder("<style>")
        builder.append(buildImgStyle())
        builder.append(buildTextStyle())
        builder.append(buildTitleStyle())
        builder.append("</style>")
        return builder
    }

    private fun buildImgStyle(): CharSequence {
        val builder = StringBuilder("img{")
        builder.append("display:inline;")
        builder.append("height:auto;")
        builder.append("max-width:100%;")
        builder.append("}")
        return builder
    }

    private fun buildTextStyle(): CharSequence {
        val builder = StringBuilder("html, body{")
        builder.append("text-align:justify;")
        builder.append("text-justify:auto;")
        builder.append("font-family: 'Roboto' sans-serif;")
        builder.append("}")
        return builder
    }

    private fun buildTitleStyle(): CharSequence {
        val builder = StringBuilder("h1{")
        builder.append("text-align:center;")
        builder.append("font-size:150%;")
        builder.append("}")
        return builder
    }
}

