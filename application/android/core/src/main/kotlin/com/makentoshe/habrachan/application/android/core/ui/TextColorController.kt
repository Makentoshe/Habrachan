package com.makentoshe.habrachan.application.android.core.ui

import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorRes
import com.makentoshe.habrachan.application.android.R

class TextColorController(private val textView: TextView) {

    fun setPositiveColor(context: Context) {
        setColor(context, R.color.positive)
    }

    fun setNegativeColor(context: Context) {
        setColor(context, R.color.negative)
    }

    fun setColor(context: Context, @ColorRes colorResource: Int) {
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorResource, context.theme)
        } else {
            context.resources.getColor(colorResource)
        }
        textView.setTextColor(color)
    }
}
