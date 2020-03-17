package com.makentoshe.habrachan.common.navigation

import ru.terrakok.cicerone.Router

class Router : Router() {

    fun customReplace(screen: Screen) {
        executeCommands(CustomReplace(screen))
    }
}
