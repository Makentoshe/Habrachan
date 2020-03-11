package com.makentoshe.habrachan.common.ui

import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorRes
import com.makentoshe.habrachan.R

class TextColorController(private val textView: TextView) {

    fun setColorBy(context: Context, score: Int) = when {
        score > 0 -> setPositiveColor(context)
        score < 0 -> setNegativeColor(context)
        else -> clearColor(context)
    }

    fun setPositiveColor(context: Context) {
        setColor(context, R.color.positive)
    }

    fun setNegativeColor(context: Context) {
        setColor(context, R.color.negative)
    }

    fun clearColor(context: Context) {
        setColor(context, android.R.color.white)
    }

    private fun setColor(context: Context, @ColorRes colorResource: Int) {
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorResource, context.theme)
        } else {
            context.resources.getColor(colorResource)
        }
        textView.setTextColor(color)
    }
}
