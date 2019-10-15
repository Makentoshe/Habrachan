package com.makentoshe.habrachan.model.post

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.RequiresApi
import com.makentoshe.habrachan.common.cache.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException

class PublicationParser(
    private val imageCache: Cache<String, Drawable>
) {

    private val imageGetter = Html.ImageGetter { url ->
        try {
            val drawable = imageCache.get(url)
            if (drawable != null) return@ImageGetter drawable else return@ImageGetter null
        } catch (ste: SocketTimeoutException) {
            null
        }
    }

    fun parse(html: String): Spanned {
        val spannable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            parseNougat(html)
        } else {
            parseDefault(html)
        }
        return spannable
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun parseNougat(html: String): Spanned {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT, imageGetter, null)
    }

    private fun parseDefault(html: String): Spanned {
        return Html.fromHtml(html)
    }
}
