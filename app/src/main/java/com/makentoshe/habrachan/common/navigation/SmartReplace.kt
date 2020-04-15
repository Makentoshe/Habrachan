package com.makentoshe.habrachan.common.navigation

import ru.terrakok.cicerone.commands.Command

/**
 * Replaces the current screen.
 *
 * If [screen] was not added yet: performs base adding action.
 * If [screen] was already added: just lets come up to top.
 * Do nothing otherwise.
 */
class SmartReplace(val screen: Screen) : Command