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


interface PublicationParser {
    fun parse(html: String): Spanned
}

class OkHttpPublicationParser(
    private val client: OkHttpClient,
    private val imageCache: Cache<String, Drawable>,
    private val converter: Converter
) : PublicationParser {

    private val imageGetter = Html.ImageGetter { url ->
        try {
            val drawable = imageCache.get(url)
            if (drawable != null) return@ImageGetter drawable

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val stream = response.body?.byteStream() ?: return@ImageGetter null
            Drawable.createFromStream(stream, url).apply {
                setBounds(0, 0, converter.px2dp(intrinsicWidth).toInt(), converter.px2dp(intrinsicHeight).toInt())
            }.also { imageCache.set(url, it) }
        } catch (ste: SocketTimeoutException) {
            null
        }
    }

    override fun parse(html: String): Spanned {
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
