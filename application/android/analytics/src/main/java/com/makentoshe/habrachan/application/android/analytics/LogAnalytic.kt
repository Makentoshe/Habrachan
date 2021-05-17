package com.makentoshe.habrachan.application.android.analytics

import android.util.Log
import com.makentoshe.habrachan.application.android.analytics.event.AnalyticEvent

open class LogAnalytic: Analytic {

    override fun capture(event: AnalyticEvent) {
        consoleDebug(event.eventTitle, event.eventMessage)
    }

    private fun consoleDebug(tag: String, message: String) {
        if (!enableConsoleLog) return
        Log.d("$tag", message)
    }

    companion object Factory {
        private var enableConsoleLog: Boolean = true

        fun registry(isDebug: Boolean) {
            enableConsoleLog = isDebug
        }
    }
}