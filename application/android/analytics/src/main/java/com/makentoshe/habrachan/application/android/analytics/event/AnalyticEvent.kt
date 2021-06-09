package com.makentoshe.habrachan.application.android.analytics.event

import kotlin.reflect.KClass

interface AnalyticEvent {
    val eventTitle: String
    val eventMessage: String
}

/** Most default way to build an AnalyticEvent instance (except default interface implementation) */
fun analyticEvent(title: String, message: String) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String = message
}

/** Allows to define eventTitle using class name */
fun analyticEvent(`class`: KClass<Any>, message: String) = analyticEvent(`class`.simpleName.toString(), message)

/** Allows to define eventTitle from a current call context */
fun Any.analyticEvent(message: String) = analyticEvent(this::class.java.simpleName, message)

/**
 * Allows to create message in the [AnalyticEvent] context.
 *
 * It may be efficient if there will be any builders uses AnalyticEvent as an initial point
 * */
fun analyticEvent(title: String, message: AnalyticEvent.() -> String) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String
        get() = message()
}
