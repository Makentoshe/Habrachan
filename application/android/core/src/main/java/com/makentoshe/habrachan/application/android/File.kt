package com.makentoshe.habrachan.application.android

import java.io.File

internal val imageFileExtensions = arrayOf("jpg", "png", "jpeg")

val File.isImage: Boolean
    get() = imageFileExtensions.contains(extension)