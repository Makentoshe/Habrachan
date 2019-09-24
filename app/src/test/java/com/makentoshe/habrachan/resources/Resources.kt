package com.makentoshe.habrachan.resources

import java.io.File

val testResourcesDirectory: File
    get() = StringBuilder(File("").absolutePath).append(File.separator)
        .append("src").append(File.separator)
        .append("test").append(File.separator)
        .append("java").append(File.separator)
        .append("com").append(File.separator)
        .append("makentoshe").append(File.separator)
        .append("habrachan").append(File.separator)
        .append("resources").toString().let(::File)
