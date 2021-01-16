package com.makentoshe.habrachan.application.android.screen.comments.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import kotlin.math.roundToInt

// TODO add top margin for separator
class DiscussionCommentSeparatorItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var mDivider = ContextCompat.getDrawable(context, R.drawable.drawable_divider)!!

    private val mBounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) return
        super.onDraw(c, parent, state)
        drawVertical(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right, parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val child = parent.getChildAt(0) ?: return
        parent.getDecoratedBoundsWithMargins(child, mBounds)
        val bottom = mBounds.bottom + child.translationY.roundToInt()
        val top = bottom - mDivider.intrinsicHeight
        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(canvas)
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect[0, 0, 0] = mDivider.intrinsicHeight
        } else {
            outRect.setEmpty()
        }
    }
}