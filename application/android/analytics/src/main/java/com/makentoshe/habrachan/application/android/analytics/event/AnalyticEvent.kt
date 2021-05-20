package com.makentoshe.habrachan.application.android.analytics.event

interface AnalyticEvent {
    val eventTitle: String
    val eventMessage: String
}

fun analyticEvent(title: String, message: String) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String = message
}
