package com.makentoshe.habrachan.application.android.common.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.android.support.FragmentParams
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command

/** Fragment adds to top of manager instead default replacing */
class StackSupportAppNavigator(
    activity: FragmentActivity, fragmentManager: FragmentManager, @IdRes container: Int
) : SupportAppNavigator(activity, fragmentManager, container) {

    override fun applyCommand(command: Command) {
        if (command is Stack) {
            fragmentStack(command)
        } else super.applyCommand(command)
    }

    /** Fragment forward applies screen to the top and does not hides(replaces) previous */
    private fun fragmentStack(command: Stack) {
        val screen = command.screen as SupportAppScreen

        val fragmentParams = screen.fragmentParams
        val fragment = if (fragmentParams == null) createFragment(screen) else null

        forwardFragmentInternal(command, screen, fragmentParams, fragment)
    }

    private fun forwardFragmentInternal(
        command: Command, screen: SupportAppScreen, fragmentParams: FragmentParams?, fragment: Fragment?
    ) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        setupFragmentTransaction(command, fragmentManager.findFragmentById(containerId), fragment, fragmentTransaction)
        addFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment)
        fragmentTransaction.addToBackStack(screen.screenKey).commit()
        localStackCopy.add(screen.screenKey)
    }


    private fun addFragmentInternal(
        transaction: FragmentTransaction, screen: SupportAppScreen, params: FragmentParams?, fragment: Fragment?
    ) = when {
        params != null -> transaction.add(containerId, params.fragmentClass, params.arguments)
        fragment != null -> transaction.add(containerId, fragment)
        else -> throw IllegalArgumentException("Either 'params' or 'fragment' shouldn't be null for " + screen.screenKey)
    }
}