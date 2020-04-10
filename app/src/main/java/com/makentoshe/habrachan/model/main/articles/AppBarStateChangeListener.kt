package com.makentoshe.habrachan.model.main.articles

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs

abstract class AppBarStateChangeListener : OnOffsetChangedListener {

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        mCurrentState = when {
            i == 0 -> onExpanded(appBarLayout)
            abs(i) >= appBarLayout.totalScrollRange -> onCollapsed(appBarLayout)
            else -> onIdle(appBarLayout)
        }
    }

    private fun onIdle(appBarLayout: AppBarLayout): State {
        if (mCurrentState != State.IDLE) {
            onStateChanged(appBarLayout, State.IDLE)
        }
        return State.IDLE
    }

    private fun onExpanded(appBarLayout: AppBarLayout): State {
        if (mCurrentState != State.EXPANDED) {
            onStateChanged(appBarLayout, State.EXPANDED)
        }
        return State.EXPANDED
    }

    private fun onCollapsed(appBarLayout: AppBarLayout): State {
        if (mCurrentState != State.COLLAPSED) {
            onStateChanged(appBarLayout, State.COLLAPSED)
        }
        return State.COLLAPSED
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }
}

