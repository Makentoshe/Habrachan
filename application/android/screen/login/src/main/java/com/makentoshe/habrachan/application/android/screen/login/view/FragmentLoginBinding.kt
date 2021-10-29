package com.makentoshe.habrachan.application.android.screen.login.view

import android.view.View
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.screen.login.databinding.FragmentLoginBinding

fun FragmentLoginBinding.onCookiesLoaded() {
    fragmentLoginProgress.visibility = View.GONE
    fragmentLoginWebview.visibility = View.VISIBLE

    fragmentLoginExceptionTitle.visibility = View.GONE
    fragmentLoginExceptionMessage.visibility = View.GONE
    fragmentLoginExceptionRetry.visibility = View.GONE
}

fun FragmentLoginBinding.onFailureCaused(entry: ExceptionEntry) {
    fragmentLoginProgress.visibility = View.GONE
    fragmentLoginWebview.visibility = View.GONE

    fragmentLoginExceptionTitle.visibility = View.VISIBLE
    fragmentLoginExceptionTitle.text = entry.title

    fragmentLoginExceptionMessage.visibility = View.VISIBLE
    fragmentLoginExceptionMessage.text = entry.message

    fragmentLoginExceptionRetry.visibility = View.VISIBLE
}

fun FragmentLoginBinding.onProgressState() {
    fragmentLoginProgress.visibility = View.VISIBLE
    fragmentLoginWebview.visibility = View.GONE

    fragmentLoginExceptionTitle.visibility = View.GONE
    fragmentLoginExceptionMessage.visibility = View.GONE
    fragmentLoginExceptionRetry.visibility = View.GONE
}

