package com.makentoshe.habrachan.application.android.analytics

import com.makentoshe.habrachan.application.android.analytics.event.AnalyticEvent

interface Analytic {
    fun capture(event: AnalyticEvent)
}