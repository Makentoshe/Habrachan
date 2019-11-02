package com.makentoshe.habrachan.model.main.posts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentActivity

/** Broadcast receiver for posts refresh events */
class PostsBroadcastReceiver : BroadcastReceiver() {

    private val listeners = ArrayList<() -> Unit>()

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.hasExtra(ACTION_REFRESH)) {
            listeners.forEach { it.invoke() }
        }
    }

    fun addOnReceiveListener(action: () -> Unit) {
        listeners.add(action)
    }

    fun register(activity: FragmentActivity) : PostsBroadcastReceiver {
        val filter = IntentFilter(ACTION_REFRESH)
        activity.registerReceiver(this, filter)
        return this
    }

    fun unregister(activity: FragmentActivity) {
        activity.unregisterReceiver(this)
    }

    companion object {
        private const val ACTION_REFRESH = "PostsRefresh"

        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(ACTION_REFRESH)
            intent.putExtra(
                ACTION_REFRESH,
                ACTION_REFRESH
            )
            context.sendBroadcast(intent)
        }

        /** Registers or create and registers a new broadcast receiver */
        fun registerReceiver(
            activity: FragmentActivity, receiver: PostsBroadcastReceiver? = null
        ): PostsBroadcastReceiver {
            val receiver = receiver ?: PostsBroadcastReceiver()
            val filter = IntentFilter(ACTION_REFRESH)
            activity.registerReceiver(receiver, filter)
            return receiver
        }
    }

}