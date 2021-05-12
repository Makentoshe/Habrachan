package com.makentoshe.habrachan.application.android.analytics.event

sealed class AnalyticEvent {
    abstract val eventTitle: String

    sealed class Exception(override val eventTitle: String, open val exception: Throwable) : AnalyticEvent() {
        data class Unhandled(override val exception: Throwable) : Exception("UnhandledException", exception)
    }

    sealed class Screen: AnalyticEvent() {
        abstract val screenName: String

        sealed class Articles: Screen() {
            override val screenName: String = "ArticlesScreen"

            class SearchQuery(val query: String?): Articles() {
                override val eventTitle: String = "Articles search query"
            }
        }
    }
}