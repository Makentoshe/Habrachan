package com.makentoshe.habrachan.model.post

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import org.sufficientlysecure.htmltextview.HtmlTextView
import java.io.File
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URI

class HtmlHttpImageGetter(
    private val container: TextView, private val matchParentWidth: Boolean = false
) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val urlDrawable = UrlDrawable()
        // get the actual source
        ImageGetterAsyncTask(urlDrawable, this, container, matchParentWidth).execute(source)
        // return reference to UrlDrawable which will asynchronously load the image specified in the src tag
        return urlDrawable
    }

    private class ImageGetterAsyncTask(
        d: UrlDrawable, imageGetter: HtmlHttpImageGetter, container: View, private val matchParentWidth: Boolean
    ) : AsyncTask<String, Void, Drawable>() {

        private val drawableReference: WeakReference<UrlDrawable> = WeakReference(d)
        private val imageGetterReference: WeakReference<HtmlHttpImageGetter> = WeakReference(imageGetter)
        private val containerReference: WeakReference<View> = WeakReference(container)
        private val resources: WeakReference<Resources> = WeakReference(container.resources)
        private var source: String = ""
        private var scale: Float = 1f

        override fun doInBackground(vararg params: String): Drawable? {
            source = params[0]
            val resources = this.resources.get() ?: return null

            val inputStream = fetch(source)

            if (File(source).extension == "svg") {
                return null
//                val svg = SVG.getFromInputStream(inputStream ?: return null)
//                val canvas = Canvas().also(svg::renderToCanvas)
//                val picture = Picture().also(canvas::drawPicture)
//                val drawable =  PictureDrawable(picture)
//                val width = (svg.documentWidth * scale).toInt()
//                val height = (svg.documentHeight * scale).toInt()
//                drawable.setBounds(0, 0, width + 1, height + 1)
//                val bitmap = drawable.toBitmap(width, height)
//                return drawable
            }

            val drawable = BitmapDrawable(resources, inputStream)
            scale = getScale(drawable)
            val width = (drawable.intrinsicWidth * scale).toInt()
            val height = (drawable.intrinsicHeight * scale).toInt()
            drawable.setBounds(0, 0, width, height)
            return drawable
        }

        override fun onPostExecute(result: Drawable?) {
            if (result == null) {
                Log.w(HtmlTextView.TAG, "Drawable result is null! (source: $source)")
                return
            }
            val urlDrawable = drawableReference.get() ?: return
            // set the correct bound according to the result from HTTP call

            val width = (result.intrinsicWidth * scale).toInt()
            val height = (result.intrinsicHeight * scale).toInt()
            urlDrawable.setBounds(0, 0, width, height)

            // change the reference of the current drawable to the result from the HTTP call
            urlDrawable.drawable = result

            val imageGetter = imageGetterReference.get() ?: return
            // redraw the image by invalidating the container
            imageGetter.container.invalidate()
            // re-set text to fix images overlapping text
            imageGetter.container.text = imageGetter.container.text
        }

        private fun getScale(drawable: Drawable): Float {
            val container = containerReference.get()
            if (!matchParentWidth || container == null) {
                return 1f
            }

            val maxWidth = container.width.toFloat()
            val originalDrawableWidth = drawable.intrinsicWidth.toFloat()

            return maxWidth / originalDrawableWidth
        }

        private fun fetch(urlString: String?): InputStream? {
            return URI.create(urlString).toURL().content as InputStream
        }
    }

    private class UrlDrawable : BitmapDrawable() {
        var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            // override the draw to facilitate refresh function later
            drawable?.draw(canvas)
        }
    }
}

