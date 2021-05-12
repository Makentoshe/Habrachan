package com.makentoshe.habrachan.application.android.analytics.event

sealed class AnalyticEvent {
    abstract val title: String

    sealed class Exception(override val title: String, open val exception: Throwable) : AnalyticEvent() {
        data class Unhandled(override val exception: Throwable) : Exception("UnhandledException", exception)
    }
}