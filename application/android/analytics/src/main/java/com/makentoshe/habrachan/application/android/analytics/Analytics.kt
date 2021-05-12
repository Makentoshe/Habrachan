package com.makentoshe.habrachan.application.android.analytics

import android.content.Context
import android.util.Log
import com.flurry.android.FlurryAgent
import com.makentoshe.habrachan.application.android.analytics.event.AnalyticEvent

open class Analytics {

    fun capture(event: AnalyticEvent) {
        when (event) {
            is AnalyticEvent.Exception.Unhandled -> {
                if (enableConsoleLog) Log.e(event.eventTitle, "Unhandled exception occur", event.exception)
                FlurryAgent.onError(event.eventTitle, "Unhandled exception occur", event.exception)
            }
            is AnalyticEvent.Screen.Articles.SearchQuery -> {
                consoleDebug(event.screenName, "${event.eventTitle}: ${event.query.toString()}")
            }
        }
    }

    private fun consoleDebug(tag: String, message: String) {
        if (!enableConsoleLog) return
        Log.d("habrachan.$tag", message)
    }

    class Flurry {
        fun registry(context: Context, analyticsApiKey: String, enableConsoleLog: Boolean) {
            FlurryAgent.Builder()
                .withLogEnabled(enableConsoleLog)
                .withCaptureUncaughtExceptions(true)
                .build(context, analyticsApiKey)

            Analytics.enableConsoleLog = enableConsoleLog
        }
    }

    companion object {
        private var enableConsoleLog: Boolean = true
    }

}
