package com.makentoshe.habrachan.application.android.screen.article.model

import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.fragment.app.FragmentActivity
import com.makentoshe.habrachan.entity.natives.Article

class ArticleShareController(private val articleId: Int) {

    private var url: String? = null

    fun setUrl(article: Article) {
        url = article.fullUrl
    }

    fun share(activity: FragmentActivity): Boolean {
        val builder = ShareCompat.IntentBuilder.from(activity)
        if (url == null) {
            builder.setText("https://habr.com/post/${articleId}/")
        } else {
            builder.setText(url)
        }

        builder.setType(Intent.ACTION_SEND).setType("text/plain").startChooser();
        return true
    }
}