package com.makentoshe.habrachan.application.android.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class ApplicationStateBroadcastReceiver : BroadcastReceiver() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)

    private val internalApplicationStateChannel = Channel<ApplicationState>()
    val applicationStateChannel: ReceiveChannel<ApplicationState>
        get() = internalApplicationStateChannel

    override fun onReceive(context: Context, intent: Intent) {
        coroutineScope.launch {
            when (intent.action) {
                APPLICATION_STATE_SIGN_IN -> internalApplicationStateChannel.send(ApplicationState.SignIn)
                APPLICATION_STATE_SIGN_OUT -> internalApplicationStateChannel.send(ApplicationState.SignOut)
            }
        }
    }

    companion object {
        private const val APPLICATION_STATE_SIGN_IN = "ApplicationStateSignIn"
        private const val APPLICATION_STATE_SIGN_OUT = "ApplicationStateSignOut"

        val filter = IntentFilter().apply {
            addAction(APPLICATION_STATE_SIGN_IN)
            addAction(APPLICATION_STATE_SIGN_OUT)
        }

        fun signIn(context: Context) {
            context.sendBroadcast(Intent(APPLICATION_STATE_SIGN_IN))
        }

        fun signOut(context: Context) {
            context.sendBroadcast(Intent(APPLICATION_STATE_SIGN_OUT))
        }
    }
}
