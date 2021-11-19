package com.makentoshe.habrachan.entity.user.get.component

import java.text.SimpleDateFormat
import java.util.*

@JvmInline
value class TimeRegistered(val string: String) {
    val date: Date get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(string)
}