package com.makentoshe.habrachan.application.android.core.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.application.android.R

class SnackbarErrorController(private val anchor: View) {

    fun displayIndefiniteMessage(message: String) {
        val snackbar = Snackbar.make(anchor, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.got_it) { snackbar.dismiss() }
        snackbar.show()
    }

    fun displayMessage(message: String) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        fun from(anchor: View): SnackbarErrorController {
            return SnackbarErrorController(anchor)
        }
    }
}

