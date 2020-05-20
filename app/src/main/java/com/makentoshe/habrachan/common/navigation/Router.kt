package com.makentoshe.habrachan.common.navigation

import com.makentoshe.habrachan.common.navigation.command.BackWithLast
import com.makentoshe.habrachan.common.navigation.command.ForwardOrReplace
import ru.terrakok.cicerone.Router

open class Router : Router() {

    fun forwardOrReplace(screen: Screen) {
        executeCommands(ForwardOrReplace(screen))
    }

    fun softExit() {
        executeCommands(BackWithLast())
    }
}
