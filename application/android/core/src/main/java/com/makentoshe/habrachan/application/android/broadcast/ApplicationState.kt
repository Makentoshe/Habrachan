package com.makentoshe.habrachan.application.android.broadcast

import java.io.Serializable

/** When global application state changes */
sealed class ApplicationState: Serializable {
    /** When user signing in to the service */
    object SignIn: ApplicationState(), Serializable

    /** When user signing out of the service */
    object SignOut: ApplicationState(), Serializable
}