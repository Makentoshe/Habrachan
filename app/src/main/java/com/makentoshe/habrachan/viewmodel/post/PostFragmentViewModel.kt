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
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
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
            val html = BaseHtmlBuilder(post, rawResourceRepository).build()
            publicationSubject.onNext(html)
        }, {

        }).let(disposables::add)
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

class BaseHtmlBuilder(
    private val post: Data,
    private val rawResourceRepository: Repository<Int, InputStream>
) {

    private val document = Jsoup.parse(post.textHtml)
    private val body = document.body()
    private val javascriptNode = createJavaScriptNode()
    private val styleNode = createStyleNode()
    private val titleNode = createTitleNode()

    fun build(): String {
        appendNode(javascriptNode)
        appendNode(styleNode)
        appendNode(titleNode)

        return body.toString()
    }

    private fun createJavaScriptNode(): Element {
        val jsBytes = rawResourceRepository.get(com.makentoshe.habrachan.R.raw.postjs)?.readBytes()
        val jsBody = if (jsBytes != null) String(jsBytes) else ""
        return Element("script").attr("type", "text/javascript").text(jsBody)
    }

    private fun createTitleNode(): Element {
        return Element("h1").text(post.title).attr("onclick", "displaymessage()")
    }

    private fun createStyleNode(): Element {
        val cssBytes = rawResourceRepository.get(com.makentoshe.habrachan.R.raw.post)?.readBytes()
        val cssBody = if (cssBytes != null) String(cssBytes) else ""
        return Element("style").text(cssBody)
    }

    private fun appendNode(firstNode: Node) {
        body.children().first().before(firstNode)
    }
}
