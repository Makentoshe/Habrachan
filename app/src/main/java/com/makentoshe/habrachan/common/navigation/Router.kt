package com.makentoshe.habrachan.common.navigation

import ru.terrakok.cicerone.Router

open class Router : Router() {

    fun forwardOrReplace(screen: Screen) {
        executeCommands(ForwardOrReplace(screen))
    }
}
