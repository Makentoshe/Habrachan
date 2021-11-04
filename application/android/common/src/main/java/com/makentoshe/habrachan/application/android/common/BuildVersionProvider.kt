package com.makentoshe.habrachan.application.android.common

import android.os.Build

interface BuildVersionProvider {
    fun is23orAbove(): Boolean
}

class BuildVersionProviderImpl : BuildVersionProvider {
    override fun is23orAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}