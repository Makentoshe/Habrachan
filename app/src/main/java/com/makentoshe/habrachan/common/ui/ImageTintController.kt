package com.makentoshe.habrachan.common.ui

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat
import com.makentoshe.habrachan.R

class ImageTintController(private val imageView: ImageView) {

    fun setTintBy(context: Context, score: Int) = when {
        score < 0 -> setNegativeTint(context)
        score > 0 -> setPositiveTint(context)
        else -> Unit
    }

    fun setTintBy(context: Context, vote: Double?) = when (vote) {
        1.0 -> setPositiveTint(context)
        -1.0 -> setNegativeTint(context)
        else -> Unit
    }

    fun setPositiveTint(context: Context) {
        setTint(context, R.color.positive)
    }

    fun setNegativeTint(context: Context) {
        setTint(context, R.color.negative)
    }

    fun clear() {
        imageView.imageTintList = null
    }

    fun setTint(context: Context, @ColorRes colorResource: Int) {
        val drawable = DrawableCompat.wrap(imageView.drawable)
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorResource, context.theme)
        } else {
            context.resources.getColor(colorResource)
        }
        drawable.setTint(color)
        imageView.setImageDrawable(drawable)
    }

}