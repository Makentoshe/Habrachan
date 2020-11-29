package com.makentoshe.habrachan.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.android.support.FragmentParams
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

/**
 * Navigator for screens that should be visible in backstack.
 * Each fragment saves its own state that allows to understand
 * when fragment was recreated and when is just returns to visible state.
 *
 * Affected commands: [Forward] and [Replace]. Other commands works as usual.
 */
class StackNavigator(
    activity: FragmentActivity, containerId: Int, fragmentManager: FragmentManager
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    override fun fragmentForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val fragmentParams = screen.fragmentParams
        val fragment = if (fragmentParams == null) createFragment(screen) else null
        forwardFragmentInternal(command, screen, fragmentParams, fragment)
    }

    override fun fragmentBack() {
        if (localStackCopy.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy.removeLast()
        } else {
            activityBack()
        }
    }

    override fun activityBack() {
        activity.finish()
    }

    override fun activityReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        // Replace activity
        if (activityIntent != null) {
            val options = createStartActivityOptions(command, activityIntent)
            checkAndStartActivity(screen, activityIntent, options)
            activity.finish()
        } else {
            fragmentReplace(command)
        }
    }

    override fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val fragmentParams = screen.fragmentParams
        val fragment = if (fragmentParams == null) createFragment(screen) else null
        if (localStackCopy.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy.removeLast()
            forwardFragmentInternal(command, screen, fragmentParams, fragment)
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            setupFragmentTransaction(
                command, fragmentManager.findFragmentById(containerId), fragment, fragmentTransaction
            )
            replaceFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun forwardFragmentInternal(
        command: Command, screen: SupportAppScreen, fragmentParams: FragmentParams?, fragment: Fragment?
    ) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        setupFragmentTransaction(
            command, fragmentManager.findFragmentById(containerId), fragment, fragmentTransaction
        )
        replaceFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment)
        fragmentTransaction.addToBackStack(screen.screenKey).commit()
        localStackCopy.add(screen.screenKey)
    }

    private fun replaceFragmentInternal(
        transaction: FragmentTransaction, screen: SupportAppScreen, params: FragmentParams?, fragment: Fragment?
    ) = when {
        params != null -> transaction.add(containerId, params.fragmentClass, params.arguments)
        fragment != null -> transaction.add(containerId, fragment)
        else -> throw IllegalArgumentException("Either 'params' or 'fragment' shouldn't be null for " + screen.screenKey)
    }


    private fun checkAndStartActivity(
        screen: SupportAppScreen, activityIntent: Intent, options: Bundle?
    ) {
        // Check if we can start activity
        if (activityIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(activityIntent, options)
        } else {
            unexistingActivity(screen, activityIntent)
        }
    }

    override fun unexistingActivity(screen: SupportAppScreen, activityIntent: Intent) {
        // Do nothing by default
    }

}
