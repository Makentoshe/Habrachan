package com.makentoshe.habrachan.application.android.screen.article.model

import android.content.Intent
import androidx.core.app.ShareCompat
import androidx.fragment.app.FragmentActivity

// TODO make fullUrl to article as a reference
class ArticleShareController(private val articleId: Int) {

    fun share(activity: FragmentActivity): Boolean {
        val builder = ShareCompat.IntentBuilder.from(activity)
        builder.setText("https://habr.com/post/${articleId}/")

        builder.setType(Intent.ACTION_SEND).setType("text/plain").startChooser();
        return true
    }
}