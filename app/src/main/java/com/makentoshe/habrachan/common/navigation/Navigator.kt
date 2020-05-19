package com.makentoshe.habrachan.common.navigation


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.commands.*
import java.util.*

/**
 * Navigator is a class which manages application screens using Fragment.
 */
class Navigator(
    private val activity: FragmentActivity,
    private val containerId: Int,
    private val fragmentManager: FragmentManager = activity.supportFragmentManager
) : ru.terrakok.cicerone.Navigator {

    private lateinit var localStackCopy: LinkedList<String>

    override fun applyCommands(commands: Array<Command>) {
        fragmentManager.executePendingTransactions()

        //copy stack before apply commands
        copyStackToLocal()

        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun copyStackToLocal() {
        localStackCopy = LinkedList()

        val stackSize = fragmentManager.backStackEntryCount
        for (i in 0 until stackSize) {
            localStackCopy!!.add(fragmentManager.getBackStackEntryAt(i).name!!)
        }
    }

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    private fun applyCommand(command: Command) {
        when (command) {
            is Forward -> forward(command)
            is ForwardOrReplace -> forwardOrReplace(command)
            is Replace -> replace(command)
            is BackTo -> backTo(command)
            is Back -> back()
        }
    }

    private fun forward(command: Forward) = internalForward(command, command.screen as Screen)

    private fun forwardOrReplace(command: ForwardOrReplace) {
        if (localStackCopy.contains(command.screen.screenKey)) {
            internalSoftReplace(command, command.screen)
        } else {
            internalForward(command, command.screen)
        }
    }

    private fun back() {
        if (localStackCopy!!.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy!!.removeLast()
        } else {
            activityBack()
        }
    }

    private fun activityBack() = activity.finish()

    private fun replace(command: Replace) {
        val screen = command.screen as Screen
        val newFragment = createFragment(screen)

        if (localStackCopy!!.size > 0) {
            fragmentManager.popBackStack()
            localStackCopy!!.removeLast()

            val fragmentTransaction = fragmentManager.beginTransaction()

            setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                newFragment,
                fragmentTransaction
            )

            fragmentTransaction
                .replace(containerId, newFragment)
                .addToBackStack(screen.screenKey)
                .commit()
            localStackCopy!!.add(screen.screenKey)

        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()

            setupFragmentTransaction(
                command,
                fragmentManager.findFragmentById(containerId),
                newFragment,
                fragmentTransaction
            )

            fragmentTransaction.replace(containerId, newFragment).commit()
        }
    }

    /**
     * Performs [BackTo] command transition
     */
    private fun backTo(command: BackTo) {
        if (command.screen == null) {
            backToRoot()
        } else {
            val key = command.screen.screenKey
            val index = localStackCopy!!.indexOf(key)
            val size = localStackCopy!!.size

            if (index != -1) {
                for (i in 1 until size - index) {
                    localStackCopy!!.removeLast()
                }
                fragmentManager.popBackStack(key, 0)
            } else {
                backToUnexisting(command.screen as Screen)
            }
        }
    }

    private fun backToRoot() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        localStackCopy!!.clear()
    }

    /**
     * Override this method to setup fragment transaction [FragmentTransaction].
     * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...)
     *
     * @param command             current navigation command. Will be only [Forward] or [Replace]
     * @param currentFragment     current fragment in container
     * (for [Replace] command it will be screen previous in new chain, NOT replaced screen)
     * @param nextFragment        next screen fragment
     * @param fragmentTransaction fragment transaction
     */
    private fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    ) = Unit

    /**
     * Creates Fragment matching `screenKey`.
     *
     * @param screen screen
     * @return instantiated fragment for the passed screen
     */
    private fun createFragment(screen: Screen) = screen.fragment

    /**
     * Called when we tried to fragmentBack to some specific screen (via [BackTo] command),
     * but didn't found it.
     *
     * @param screen screen
     */
    private fun backToUnexisting(screen: Screen) = backToRoot()

    /** Performs base forward navigation command */
    private fun internalForward(command: Command, screen: Screen) {
        val fragment = createFragment(screen)

        val fragmentTransaction = fragmentManager.beginTransaction()

        setupFragmentTransaction(
            command,
            fragmentManager.findFragmentById(containerId),
            fragment,
            fragmentTransaction
        )

        fragmentTransaction
            .add(containerId, fragment, screen.screenKey)
            .addToBackStack(screen.screenKey)
            .commit()
        localStackCopy.add(screen.screenKey)
    }

    /** Performs replace command with saving replaced screen in memory */
    // Todo replace hide/show to detach/attach methods
    private fun internalSoftReplace(command: Command, screen: Screen) {
        val fragment = fragmentManager.findFragmentByTag(screen.screenKey)!!
        // remove selected screen from stack
        localStackCopy.remove(screen.screenKey)
        // begin transaction
        val fragmentTransaction = fragmentManager.beginTransaction()
        setupFragmentTransaction(
            command, fragmentManager.findFragmentById(containerId), fragment, fragmentTransaction
        )
        // hide all fragments in stack
        localStackCopy.forEach { screenKey ->
            fragmentTransaction.hide(fragmentManager.findFragmentByTag(screenKey)!!)
        }
        // add selected screen to stack
        localStackCopy.add(screen.screenKey)
        // show selected screen
        fragmentTransaction.show(fragment).commit()
    }
}