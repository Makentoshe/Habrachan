package com.makentoshe.habrachan.application.android.common.navigation

import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.commands.Command

/**
 * Fragment forward applies screen to the top and does not hides(replaces) previous
 */
class Stack(val screen: Screen) : Command