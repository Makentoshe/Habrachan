package com.makentoshe.habrachan.common.ui.softkeyboard

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

class SoftKeyboardController {

    fun addVisibilityChangeListener(activity: Activity, listener: OnSoftKeyboardStateChangedListener) {
        val activityRootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        val globalLayoutListener = SoftKeyboardGlobalLayoutListener(activityRootView, listener)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    fun addVisibilityChangeListener(activity: Activity, listener: (Boolean) -> Unit) {
        addVisibilityChangeListener(activity, object :
            OnSoftKeyboardStateChangedListener {
            override fun onStateChanged(isOpen: Boolean) = listener.invoke(isOpen)
        })
    }

    fun hide(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

}
