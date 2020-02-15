package com.makentoshe.habrachan.common.database

import android.content.Context

class ImageDatabase(private val context: Context) {

    fun avatars() = AvatarDao(context)
}
