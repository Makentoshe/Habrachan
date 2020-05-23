package com.makentoshe.habrachan.navigation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.android.support.FragmentParams
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import java.util.*

/**
 * Navigator for using with bottom navigation bar
 * and allows to save fragments from another tabs in memory.
 *
 * Affected commands: [Replace] and [Back]. Other commands will be ignored.
 */
open class SlipNavigator(
    activity: FragmentActivity, containerId: Int, fragmentManager: FragmentManager
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    private val replaceStackCopy = LinkedList<String>()

    override fun applyCommand(command: Command) = when (command) {
        is Replace -> activityReplace(command)
        is Back -> fragmentBack()
        else -> Unit
    }

    override fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen

        replaceStackCopy.add(screen.screenKey)
        skipFirst = true

        val existsFragment = fragmentManager.findFragmentByTag(screen.screenKey)
        val currentFragment = fragmentManager.findFragmentById(containerId)

        val fragmentTransaction = fragmentManager.beginTransaction()
        setupFragmentTransaction(command, currentFragment, existsFragment, fragmentTransaction)

        fragmentManager.fragments.forEach { fragment -> fragmentTransaction.hide(fragment) }

        if (existsFragment != null) {
            fragmentTransaction.show(existsFragment).addToBackStack(null).commit()
        } else {
            val fragmentParams = screen.fragmentParams
            val fragment = if (fragmentParams == null) createFragment(screen) else null
            forwardFragmentInternal(fragmentTransaction, screen, fragmentParams, fragment)
            fragmentTransaction.addToBackStack(screen.screenKey).commit()
            localStackCopy.add(screen.screenKey)
        }
    }

    private fun forwardFragmentInternal(
        transaction: FragmentTransaction, screen: SupportAppScreen, params: FragmentParams?, fragment: Fragment?
    ) = when {
        params != null -> transaction.add(containerId, params.fragmentClass, params.arguments, screen.screenKey)
        fragment != null -> transaction.add(containerId, fragment, screen.screenKey)
        else -> throw IllegalArgumentException("Either 'params' or 'fragment' shouldn't be null for " + screen.screenKey)
    }

    private var skipFirst = true

    override fun fragmentBack() {
        if (replaceStackCopy.size < 0) {
            return super.activityBack()
        }

        if (skipFirst) {
            skipFirst = false
            replaceStackCopy.removeLast()
        }

        val screenKey = replaceStackCopy.removeLast()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentManager.fragments.forEach { fragment -> fragmentTransaction.hide(fragment) }

        val fragment = fragmentManager.findFragmentByTag(screenKey)!!

        fragmentTransaction.show(fragment).commit()
    }
}