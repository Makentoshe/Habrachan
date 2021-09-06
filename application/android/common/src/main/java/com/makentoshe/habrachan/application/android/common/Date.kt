package com.makentoshe.habrachan.application.android.common

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date


fun Date.time(context: Context, string: Int): String {
    val date = SimpleDateFormat("dd MMMM yyyy").format(this)
    val time = SimpleDateFormat("HH:mm").format(this)
    return context.getString(string, date, time)
}
