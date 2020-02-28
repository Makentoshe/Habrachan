package com.makentoshe.habrachan.ui.articles

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.bottomappbar.BottomAppBar

class BottomBarBehavior(
    context: Context, attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<BottomAppBar>(context, attrs) {

    private var height = 0
    private var currentState =
        STATE_SCROLLED_UP
    private var additionalHiddenOffsetY = 0
    private var currentAnimator: ViewPropertyAnimator? = null

    override fun onLayoutChild(parent: CoordinatorLayout, child: BottomAppBar, layoutDirection: Int): Boolean {
        val paramsCompat = child.layoutParams as MarginLayoutParams
        height = child.measuredHeight + paramsCompat.bottomMargin
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomAppBar,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomAppBar,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed < 0) {
            slideDown(child)
        } else if (dyConsumed > 0) {
            slideUp(child)
        }
    }

    /**
     * Perform an animation that will slide the child from it's current position to be totally on the
     * screen.
     */
    private fun slideUp(child: BottomAppBar) {
        if (currentState == STATE_SCROLLED_UP) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        currentState = STATE_SCROLLED_UP
        animateChildTo(
            child,
            0,
            ENTER_ANIMATION_DURATION.toLong(),
            AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
        )
    }

    /**
     * Perform an animation that will slide the child from it's current position to be totally off the
     * screen.
     */
    private fun slideDown(child: BottomAppBar) {
        if (currentState == STATE_SCROLLED_DOWN) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        currentState =
            STATE_SCROLLED_DOWN
        animateChildTo(
            child,
            height + additionalHiddenOffsetY,
            EXIT_ANIMATION_DURATION.toLong(),
            AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
        )
    }

    private fun animateChildTo(child: BottomAppBar, targetY: Int, duration: Long, interpolator: TimeInterpolator) {
        currentAnimator = child.animate()
            .translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }
                }
            )
    }

    companion object {
        private const val ENTER_ANIMATION_DURATION = 225
        private const val EXIT_ANIMATION_DURATION = 175

        private const val STATE_SCROLLED_DOWN = 1
        private const val STATE_SCROLLED_UP = 2
    }
}

