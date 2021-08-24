package com.makentoshe.habrachan.application.android.common.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

open class StackRouter : Router() {
    fun stack(screen: SupportAppScreen) {
        executeCommands(Stack(screen))
    }
}