package com.makentoshe.habrachan.application.android.analytics.event

interface AnalyticEvent {
    val eventTitle: String
    val eventMessage: String
    val eventException: Throwable?
}

fun analyticEvent(title: String, message: String, exception: Throwable? = null) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String = message
    override val eventException: Throwable? = exception
}
