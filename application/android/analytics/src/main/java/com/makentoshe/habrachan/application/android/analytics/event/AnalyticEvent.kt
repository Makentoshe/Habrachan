package com.makentoshe.habrachan.application.android.analytics.event

import kotlin.reflect.KClass

interface AnalyticEvent {
    val eventTitle: String
    val eventMessage: String
    val eventException: Throwable?
}

/** Most default way to build an AnalyticEvent instance (except default interface implementation) */
fun analyticEvent(title: String, message: String, exception: Throwable? = null) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String = message
    override val eventException: Throwable? = exception
}

/**
 * Allows to create message in the [AnalyticEvent] context.
 *
 * It may be efficient if there will be any builders uses AnalyticEvent as an initial point
 * */
fun analyticEvent(title: String, throwable: Throwable? = null, message: AnalyticEvent.() -> String) = object : AnalyticEvent {
    override val eventTitle: String = title
    override val eventMessage: String
        get() = message()
    override val eventException = throwable
}

/** Allows to define eventTitle using class name */
fun analyticEvent(`class`: KClass<Any>, message: String, throwable: Throwable? = null): AnalyticEvent {
    return analyticEvent(`class`.simpleName.toString(), message, throwable)
}

/** Allows to define eventTitle from a current call context */
fun Any.analyticEvent(message: String = "", throwable: Throwable? = null): AnalyticEvent {
    val title = "${this::class.java.simpleName}(${Integer.toHexString(this.hashCode())})"
    return analyticEvent(title, message, throwable)
}

fun Any.analyticEvent(throwable: Throwable? = null, message: AnalyticEvent.() -> String): AnalyticEvent {
    val title = "${this::class.java.simpleName}(${Integer.toHexString(this.hashCode())})"
    return analyticEvent(title, throwable, message)
}

fun Any.analyticEventAddrless(throwable: Throwable? = null, message: AnalyticEvent.() -> String): AnalyticEvent {
    return analyticEvent(this::class.java.simpleName, throwable, message)
}
