package com.makentoshe.habrachan.application.android.analytics

import android.content.Context
import com.flurry.android.FlurryAgent
import com.makentoshe.habrachan.application.android.analytics.event.AnalyticEvent

open class Analytics {

    fun capture(event: AnalyticEvent) {
        when (event) {
            is AnalyticEvent.Exception.Unhandled -> {
                FlurryAgent.onError(event.title, "Exception wasn't handled", event.exception)
            }
        }
    }

    class Flurry {
        fun registry(context: Context, analyticsApiKey: String, enableConsoleLog: Boolean) {
            FlurryAgent.Builder().withLogEnabled(enableConsoleLog).withCaptureUncaughtExceptions(true).build(context, analyticsApiKey)
        }
    }

}
