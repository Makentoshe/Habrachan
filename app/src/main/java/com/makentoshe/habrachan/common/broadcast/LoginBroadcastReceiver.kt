package com.makentoshe.habrachan.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Broadcast receiver observes login events.
 */
class LoginBroadcastReceiver : BroadcastReceiver() {

    private val subject = PublishSubject.create<String>()
    val observable: Observable<String> = subject

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action != ACTION) return
        subject.onNext(intent.getStringExtra(LOGIN) ?: return)
    }

    companion object {

        fun send(context: Context, login: String) = context.sendBroadcast(Intent(ACTION).putExtra(LOGIN, login))

        private const val LOGIN = "HabrachanLogin"
        const val ACTION = "HabrachanLoginAction"
    }
}