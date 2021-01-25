package com.makentoshe.habrachan.application.android.screen.articles.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.makentoshe.habrachan.R

class ArticleItemDecoration(
    @ColorInt
    color: Int
) : ItemDecoration() {

    companion object {
        private const val itemWidth = 2

        fun from(context: Context): ArticleItemDecoration {
            return ArticleItemDecoration(ContextCompat.getColor(context, R.color.brand_dark))
        }
    }

    private val paint = Paint().apply {
        this.color = color
        this.textAlign = Paint.Align.CENTER
        this.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // this will iterate over every visible view
        for (i in 0 until parent.childCount) {
            // get the view
            val view = parent.getChildAt(i)
            // and finally draw the separator
            drawItemSeparator(c, view)
        }
    }

    private fun drawItemSeparator(canvas: Canvas, view: View) = canvas.drawLine(
        view.left.toFloat(),
        (view.bottom + itemWidth).toFloat(),
        view.right.toFloat(),
        (view.bottom + itemWidth).toFloat(),
        paint
    )

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // calculate rect for item separator
        outRect[0, 0, 0] = itemWidth // left, top, right, bottom
    }
}