package com.makentoshe.habrachan.entity.component

import java.text.SimpleDateFormat
import java.util.*

interface TimePublished {
    val timePublishedString: String
    val timePublishedDate: Date
}

fun timePublished(timePublishedString: String) = object : TimePublished {
    override val timePublishedString: String = timePublishedString
    override val timePublishedDate: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(timePublishedString)
}