package com.makentoshe.habrachan.application.android.common.strings

import android.content.Context
import androidx.annotation.StringRes

class BundledStringsProvider(private val context: Context) : StringsProvider<Int> {
    override fun getString(@StringRes key: Int): String {
        return context.getString(key)
    }
}