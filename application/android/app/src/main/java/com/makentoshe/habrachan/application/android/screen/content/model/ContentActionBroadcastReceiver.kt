package com.makentoshe.habrachan.application.android.screen.content.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class ContentActionBroadcastReceiver(private val scope: CoroutineScope) : BroadcastReceiver() {

    private val internalChannel = Channel<Action>()
    val channel: ReceiveChannel<Action> = internalChannel

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Action.Download.string -> scope.launch {
                internalChannel.send(Action.Download)
            }
            Action.Share.string -> scope.launch {
                internalChannel.send(Action.Share)
            }
            else -> throw IllegalStateException("")
        }
    }

    private val filter = IntentFilter().apply {
        addAction(Action.Download.string)
        addAction(Action.Share.string)
    }

    fun register(activity: FragmentActivity, lifecycle: Lifecycle): ContentActionBroadcastReceiver {
        activity.registerReceiver(this, filter)
        lifecycle.addObserver(LifecycleObserver(activity, this))
        return this
    }

    private class LifecycleObserver(
        private val activity: FragmentActivity, private val broadcastReceiver: ContentActionBroadcastReceiver
    ) : androidx.lifecycle.LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = try {
            activity.unregisterReceiver(broadcastReceiver)
        } catch (exception: IllegalStateException) {
            // Ignore java.lang.IllegalArgumentException: Receiver not registered: com.makentoshe.habrachan.application.android.screen.content.model.ContentActionBroadcastReceiver@ac95c3a
        }

    }

    sealed class Action {
        abstract val string: String

        object Share : Action() {
            override val string: String = SHARE_CONTENT_ACTION
        }

        object Download : Action() {
            override val string: String = DOWNLOAD_CONTENT_ACTION
        }
    }

    companion object {
        private const val SHARE_CONTENT_ACTION = "ShareContentAction"
        private const val DOWNLOAD_CONTENT_ACTION = "DownloadContentAction"

        fun actionDownload(context: Context) {
            context.sendBroadcast(Intent(Action.Download.string))
        }

        fun actionShare(context: Context) {
            context.sendBroadcast(Intent(Action.Share.string))
        }
    }
}