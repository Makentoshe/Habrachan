package com.makentoshe.habrachan.application.android.screen.user.view

import android.view.View
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.screen.user.R
import com.makentoshe.habrachan.application.android.screen.user.databinding.FragmentUserBinding
import com.makentoshe.habrachan.application.common.arena.user.get.*
import java.text.SimpleDateFormat
import java.util.*

fun FragmentUserBinding.onProgressState() {
    fragmentUserGlobalProgress.visibility = View.VISIBLE
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

fun FragmentUserBinding.onUserSuccess(user: UserFromArena) {
    fragmentUserGlobalProgress.visibility = View.GONE

    fragmentUserAvatar.visibility = View.VISIBLE

    fragmentUserToolbar.title = user.login.value.string

    fragmentUserFullname.text = user.fullname.value.string
    fragmentUserFullname.visibility = View.VISIBLE
    fragmentUserFullnameTitle.visibility = View.VISIBLE

    val dateFormat = this.root.context.getString(R.string.fragment_user_registered_format)
    fragmentUserRegistered.text = SimpleDateFormat(dateFormat, Locale.getDefault()).format(user.registered.value.date)
    fragmentUserRegistered.visibility = View.VISIBLE
    fragmentUserRegisteredTitle.visibility = View.VISIBLE

    fragmentUserSpeciality.text = user.speciality.value
    fragmentUserSpeciality.visibility = View.VISIBLE

    fragmentUserCountersKarma.text = user.scoreCount.value.toString()
    fragmentUserCountersRating.text = user.rating.value.toString()
    fragmentUserCountersFollowers.text = user.followersCount.value.toString()
    fragmentUserCountersFollowing.text = user.followingCount.value.toString()
    fragmentUserCounters.visibility = View.VISIBLE
}
