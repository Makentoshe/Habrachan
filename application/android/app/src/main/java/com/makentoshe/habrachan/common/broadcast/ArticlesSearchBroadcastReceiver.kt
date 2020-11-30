package com.makentoshe.habrachan.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ArticlesSearchBroadcastReceiver : BroadcastReceiver() {

    private val broadcastSubject = PublishSubject.create<ArticlesRequestSpec>()
    val broadcastObservable: Observable<ArticlesRequestSpec> = broadcastSubject

    override fun onReceive(context: Context, intent: Intent) {
        val spec = intent.getBundleExtra(ACTION).get(ACTION) as ArticlesRequestSpec
        broadcastSubject.onNext(spec)
    }

    fun registerReceiver(context: Context) {
        context.registerReceiver(this, IntentFilter(ACTION))
    }

    companion object {

        fun sendBroadcast(context: Context, spec: ArticlesRequestSpec) {
            val bundle = Bundle()
            bundle.putSerializable(ACTION, spec)
            val intent = Intent(ACTION).putExtra(ACTION, bundle)
            context.sendBroadcast(intent)
        }

        private const val ACTION = "ArticlesRequestSpec"
    }
}