package com.makentoshe.habrachan.application.android.analytics

import com.makentoshe.habrachan.application.android.analytics.event.AnalyticEvent

open class Analytics(private vararg val analytics: Analytic) : Analytic {

    override fun capture(event: AnalyticEvent) {
        analytics.forEach { analytic -> analytic.capture(event) }
    }
}

