package com.makentoshe.habrachan.common.navigation.command

import com.makentoshe.habrachan.common.navigation.Screen
import ru.terrakok.cicerone.commands.Command

/**
 * Opens new screen or move existing to the top
 */
class ForwardOrReplace(val screen: Screen) : Command
