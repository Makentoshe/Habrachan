package com.makentoshe.habrachan.model.article.web

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

class JavaScriptInterface {

    private val imageSubject = PublishSubject.create<String>()
    val imageObservable: Observable<String>
        get() = imageSubject.observeOn(AndroidSchedulers.mainThread())

    @android.webkit.JavascriptInterface
    fun showToast() {
        println("SAS ASA ANUS PSA")
    }

    @android.webkit.JavascriptInterface
    fun onImageClickedListener(imageSource: String) {
        imageSubject.onNext(imageSource)
    }
}