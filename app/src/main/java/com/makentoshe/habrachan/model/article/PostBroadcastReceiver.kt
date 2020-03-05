package com.makentoshe.habrachan.model.article

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class PostBroadcastReceiver @Inject constructor(): BroadcastReceiver() {

    private val imageClickedListener = ArrayList<(String, Array<String>) -> Unit>()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == IMAGE_CLICKED) {
            processImageClicked(intent)
        }
    }

    private fun processImageClicked(intent: Intent) {
        val source = intent.getStringExtra(IMAGE_CLICKED_SOURCE)
        val sources = intent.getStringArrayExtra(IMAGE_CLICKED_SOURCES)
        imageClickedListener.forEach { it.invoke(source, sources) }
    }

    fun addOnImageClickedListener(action: (String, Array<String>) -> Unit) {
        imageClickedListener.add(action)
    }

    fun registerReceiver(activity: FragmentActivity) {
        val filter = IntentFilter(IMAGE_CLICKED)
        activity.registerReceiver(this, filter)
    }

    companion object {

        private const val IMAGE_CLICKED = "PostImageClicked"
        private const val IMAGE_CLICKED_SOURCE = "PostImageClickedSource"
        private const val IMAGE_CLICKED_SOURCES = "PostImageClickedSources"

        fun sendImageClickedBroadcast(context: Context, source: String, sources: List<String>) {
            val intent = Intent(IMAGE_CLICKED)
            intent.putExtra(IMAGE_CLICKED_SOURCE, source)
            intent.putExtra(IMAGE_CLICKED_SOURCES, sources.toTypedArray())
            context.sendBroadcast(intent)
        }
    }

}
