package com.makentoshe.habrachan.model.post

import android.content.res.Resources
import android.util.TypedValue

class Converter(private val resources: Resources) {

    fun px2dp(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        )
    }

    fun px2dp(value: Int) = px2dp(value.toFloat())

}