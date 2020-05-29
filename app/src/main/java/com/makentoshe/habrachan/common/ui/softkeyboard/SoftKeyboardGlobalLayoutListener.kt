package com.makentoshe.habrachan.common.ui.softkeyboard

import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver

internal class SoftKeyboardGlobalLayoutListener(
    private val activityRootView: View,
    private val listener: OnSoftKeyboardStateChangedListener
) : ViewTreeObserver.OnGlobalLayoutListener {

    private var alreadyOpen = false
    private val rect: Rect = Rect()

    override fun onGlobalLayout() {
        val isShown = calculateCurrentVisibilityState()
        // Ignoring global layout change
        if (isShown == alreadyOpen) return
        alreadyOpen = isShown
        listener.onStateChanged(isShown)
    }

    private fun calculateCurrentVisibilityState(): Boolean {
        val estimatedKeyboardHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, estimatedKeyboardDP, activityRootView.resources.displayMetrics
        ).toInt()
        activityRootView.getWindowVisibleDisplayFrame(rect)
        val heightDiff: Int = activityRootView.rootView.height - (rect.bottom - rect.top)
        return heightDiff >= estimatedKeyboardHeight
    }

    companion object {
        private const val defaultKeyboardHeightDP = 100
        private val estimatedKeyboardDP = defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48f else 0f
    }
}