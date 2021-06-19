package com.makentoshe.habrachan.application.android

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

// TODO moved to common-core
fun Date.time(context: Context, string: Int): String {
    val date = SimpleDateFormat("dd MMMM yyyy").format(this)
    val time = SimpleDateFormat("HH:mm").format(this)
    return context.getString(string, date, time)
}
