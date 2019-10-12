package com.makentoshe.habrachan.model.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentActivity

class MainFlowBroadcastReceiver : BroadcastReceiver() {

    private val listeners = ArrayList<(Int) -> Unit>()

    override fun onReceive(context: Context?, intent: Intent) {
        val page = intent.getIntExtra(ACTION_PAGE, 0)
        listeners.forEach { it.invoke(page) }
    }

    fun addOnReceiveListener(action: (Int) -> Unit) {
        listeners.add(action)
    }

    companion object {
        private const val ACTION_PAGE = "PageUpdate"

        fun sendBroadcast(context: Context, page: Int) {
            val intent = Intent(ACTION_PAGE)
            intent.putExtra(ACTION_PAGE, page)
            context.sendBroadcast(intent)
        }

        /** Registers or create and registers a new broadcast receiver */
        fun registerReceiver(
            activity: FragmentActivity, receiver: MainFlowBroadcastReceiver? = null
        ): MainFlowBroadcastReceiver {
            val receiver = receiver ?: MainFlowBroadcastReceiver()
            val filter = IntentFilter(ACTION_PAGE)
            activity.registerReceiver(receiver, filter)
            return receiver
        }
    }

}