package com.makentoshe.habrachan.application.android.common.asset

import android.content.Context

class CommonAssetProvider(private val context: Context) {
    fun get(string: String) = Asset(string, context.assets.open(string).readBytes())
}