package com.makentoshe.habrachan.application.android.analytics

import android.util.Log
import com.makentoshe.habrachan.application.android.analytics.event.AnalyticEvent

open class LogAnalytic : Analytic {

    override fun capture(event: AnalyticEvent) {
        val eventException = event.eventException
        if (eventException != null) {
            consoleException(event.eventTitle, event.eventMessage, eventException)
        } else {
            consoleDebug(event.eventTitle, event.eventMessage)
        }
    }

    private fun consoleDebug(tag: String, message: String) {
        if (!enableConsoleLog) return
        Log.d("analytics.logger.$tag", message)
    }

    private fun consoleException(tag: String, message: String, exception: Throwable) {
        Log.e(tag, message, exception)
    }

    companion object Factory {
        private var enableConsoleLog: Boolean = true

        fun registry(isDebug: Boolean) {
            enableConsoleLog = isDebug
        }
    }
}