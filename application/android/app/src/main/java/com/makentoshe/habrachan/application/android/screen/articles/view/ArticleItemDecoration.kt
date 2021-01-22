package com.makentoshe.habrachan.application.android.screen.articles.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class ArticleItemDecoration(
    @ColorInt
    color: Int,
    @FloatRange(from = 0.0, fromInclusive = false)
    width: Float,
    @FloatRange(from = 0.0)
    private val mMarginLeft: Float,
    @FloatRange(from = 0.0)
    private val mMarginRight: Float
) : ItemDecoration() {

    private val mPaint = Paint()

    init {
        mPaint.color = color
        mPaint.strokeWidth = width
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val params = view.layoutParams as RecyclerView.LayoutParams

        // we want to retrieve the position in the list
        val position = params.viewAdapterPosition

        // and add a separator to any view but the last one
        if (position < state.itemCount) {
            outRect[0, 0, 0] = mPaint.strokeWidth.toInt() // left, top, right, bottom
        } else {
            outRect.setEmpty() // 0, 0, 0, 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // we set the stroke width before, so as to correctly draw the line we have to offset by width / 2
        val offset = (mPaint.strokeWidth / 2).toInt()

        // this will iterate over every visible view
        for (i in 0 until parent.childCount) {
            // get the view
            val view = parent.getChildAt(i)
            val params = view.layoutParams as RecyclerView.LayoutParams

            // get the position
            val position = params.viewAdapterPosition

            // and finally draw the separator
            if (position < state.itemCount) {
                c.drawLine(
                    view.left + mMarginLeft,
                    (view.bottom + offset).toFloat(),
                    view.right - mMarginRight,
                    (view.bottom + offset).toFloat(),
                    mPaint
                )
            }
        }
    }

    class Builder(private val mContext: Context) {
        private var mWidth = 0f
        private var mColor = 0
        private var mMarginLeft = 0f
        private var mMarginRight = 0f

        /**
         * Set the separator color from a resource.
         *
         * @param colorResId the resource id to use
         * @return the builder
         */
        fun colorFromResources(
            @ColorRes
            colorResId: Int
        ): Builder {
            mColor = mContext.resources.getColor(colorResId)
            return this
        }

        /**
         * Set the separator color from a color.
         *
         * @param color the color to use
         * @return the builder
         * @see android.graphics.Color
         */
        fun color(
            @ColorInt
            color: Int
        ): Builder {
            mColor = color
            return this
        }

        /**
         * Set the width of the separator.
         *
         * @param width the width in dp
         * @return the builder
         */
        fun width(
            @FloatRange(from = 0.0, fromInclusive = false)
            width: Float
        ): Builder {
            mWidth = width
            return this
        }

        /**
         * Set the margin of the separator
         *
         * @param margin the margin in dp
         * @return the builder
         */
        fun setMargin(margin: Float): Builder {
            setMargin(margin, margin)
            return this
        }

        /**
         * Set the margin of the separator
         *
         * @param left  the left margin in dp
         * @param right the right margin in dp
         * @return the builder
         */
        fun setMargin(left: Float, right: Float): Builder {
            val metrics = mContext.resources.displayMetrics
            mMarginLeft = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, left, metrics
            )
            mMarginRight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, right, metrics
            )
            return this
        }

        @get:ColorInt
        private val defaultColor: Int
            get() {
                val typedValue = TypedValue()
                val theme = mContext.theme
                theme.resolveAttribute(android.R.attr.colorControlHighlight, typedValue, true)
                return typedValue.data
            }

        /**
         * Get the configured SeparatorDecoration.
         *
         * @return the separatorDecoration
         * @see ArticleItemDecoration
         */
        fun build(): ArticleItemDecoration {
            return ArticleItemDecoration(mColor, mWidth, mMarginLeft, mMarginRight)
        }

        /**
         * Create the builder with a context to configure a SeparatorDecoration.
         *
         * @param context the context
         */
        init {
            color(defaultColor)
            width(1f)
        }
    }

    companion object {
        /**
         * A builder to easily create the decoration.
         *
         * @param context a context to use
         * @return the builder
         */
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }
}