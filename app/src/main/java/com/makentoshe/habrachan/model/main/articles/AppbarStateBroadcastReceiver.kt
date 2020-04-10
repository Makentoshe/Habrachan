package com.makentoshe.habrachan.model.main.articles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AppbarStateBroadcastReceiver : BroadcastReceiver() {

    private val subject =
        PublishSubject.create<AppBarStateChangeListener.State>()
    val observable: Observable<AppBarStateChangeListener.State>
        get() = subject

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != AppBarStateChangeListener.State::class.java.simpleName) return
        val stateString = intent.getStringExtra(AppBarStateChangeListener.State::class.java.simpleName)
        val state = AppBarStateChangeListener.State.valueOf(stateString)
        subject.onNext(state)
    }

    companion object {
        fun sendBroadcast(context: Context, state: AppBarStateChangeListener.State) {
            val intent = Intent()
                .setAction(state.javaClass.simpleName).putExtra(state.javaClass.simpleName, state.toString())
            context.sendBroadcast(intent)
        }
    }
}