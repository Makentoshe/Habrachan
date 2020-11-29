package com.makentoshe.habrachan.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Broadcast receiver observes logout events.
 */
class LogoutBroadcastReceiver : BroadcastReceiver() {

    private val subject = PublishSubject.create<Unit>()
    val observable: Observable<Unit> = subject

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action != ACTION) return
        subject.onNext(Unit)
    }

    companion object {

        fun send(context: Context) = context.sendBroadcast(Intent(ACTION))

        const val ACTION = "HabrachanLogoutAction"
    }
}
