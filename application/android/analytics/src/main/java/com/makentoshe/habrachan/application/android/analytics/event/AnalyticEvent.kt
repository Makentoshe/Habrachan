package com.makentoshe.habrachan.application.android.analytics.event

import kotlin.reflect.KClass

interface AnalyticEvent {
    val eventTitle: String
    val eventMessage: String
}

fun analyticEvent(title: String, message: String) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String = message
}

fun analyticEvent(`class`: KClass<Any>, message: String) = analyticEvent(`class`.simpleName.toString(), message)

fun Any.analyticEvent(message: String) = analyticEvent(this::class.java.simpleName, message)
