package com.makentoshe.habrachan.di.article

import com.makentoshe.habrachan.model.article.web.JavaScriptInterface
import com.makentoshe.habrachan.model.article.web.WebViewController
import com.makentoshe.habrachan.view.article.WebArticleFragment
import toothpick.config.Module
import toothpick.ktp.binding.bind

class WebArticleFragmentModule(fragment: WebArticleFragment): Module() {

    init {
        val javascriptInterface = JavaScriptInterface()
        bind<JavaScriptInterface>().toInstance(javascriptInterface)
        bind<WebViewController>().toInstance(
            WebViewController(
                fragment,
                javascriptInterface
            )
        )
    }
}