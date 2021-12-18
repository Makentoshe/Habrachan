package com.makentoshe.habrachan.application.android.screen.user.view

import android.view.View
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.screen.user.databinding.FragmentUserBinding

fun FragmentUserBinding.onProgressState() {
    fragmentUserProgress.visibility = View.VISIBLE
}

fun FragmentUserBinding.onFailureCaused(entry: ExceptionEntry<*>) {
//    fragmentLoginProgress.visibility = android.view.View.GONE
//    fragmentLoginWebview.visibility = android.view.View.GONE
//
//    fragmentLoginExceptionTitle.visibility = android.view.View.VISIBLE
//    fragmentLoginExceptionTitle.text = entry.title
//
//    fragmentLoginExceptionMessage.visibility = android.view.View.VISIBLE
//    fragmentLoginExceptionMessage.text = entry.message
//
//    fragmentLoginExceptionRetry.visibility = android.view.View.VISIBLE
}
