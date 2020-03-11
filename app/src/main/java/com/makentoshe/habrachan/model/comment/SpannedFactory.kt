package com.makentoshe.habrachan.model.article.comment

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.makentoshe.habrachan.common.repository.InputStreamRepository

class SpannedFactory(private val imageGetter: Html.ImageGetter?) {

    fun build(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
        } else {
            Html.fromHtml(html, imageGetter, null)
        }
    }

    class ImageGetter(
        private val repository: InputStreamRepository,
        private val resources: Resources
    ) : Html.ImageGetter {
        override fun getDrawable(source: String): Drawable {
            return BitmapDrawable(resources, repository.get(source))
        }
    }
}
