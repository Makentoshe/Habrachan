package com.makentoshe.habrachan.application.android.common

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.closeSoftKeyboard() {
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = requireActivity().currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Fragment.dp2px(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)


/**
 * Converts dp unit to equivalent pixels, depending on device density.
 *
 * @param id A dimension resource in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Fragment.dp2px(id: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, resources.getDimension(id), resources.displayMetrics)

/**
 * Converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @return A float value to represent dp equivalent to px value
 */
fun Fragment.px2dp(px: Float) = px / (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

/**
 * Converts device specific pixels to density independent pixels.
 *
 * @param id A dimension resource in px (pixels) unit. Which we need to convert into db
 * @return A float value to represent dp equivalent to px value
 */
fun Fragment.px2dp(id: Int) =
    resources.getDimension(id) / (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
