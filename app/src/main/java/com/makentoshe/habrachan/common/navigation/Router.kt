package com.makentoshe.habrachan.common.navigation

import ru.terrakok.cicerone.Router

open class Router : Router() {

    fun smartReplace(screen: Screen) {
        executeCommands(SmartReplace(screen))
    }
}
