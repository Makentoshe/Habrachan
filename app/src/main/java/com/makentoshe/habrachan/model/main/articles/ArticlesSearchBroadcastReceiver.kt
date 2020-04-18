package com.makentoshe.habrachan.model.main.articles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ArticlesSearchBroadcastReceiver : BroadcastReceiver() {

    private val broadcastSubject = PublishSubject.create<GetArticlesRequest.Spec2>()
    val broadcastObservable: Observable<GetArticlesRequest.Spec2> = broadcastSubject

    override fun onReceive(context: Context, intent: Intent) {
        val spec = intent.getBundleExtra(ACTION).get(ACTION) as GetArticlesRequest.Spec2
        broadcastSubject.onNext(spec)
    }

    fun registerReceiver(context: Context) {
        context.registerReceiver(this, IntentFilter(ACTION))
    }

    companion object {

        fun sendBroadcast(context: Context, spec: GetArticlesRequest.Spec2) {
            val bundle = Bundle()
            bundle.putSerializable(ACTION, spec)
            val intent = Intent(ACTION).putExtra(ACTION, bundle)
            context.sendBroadcast(intent)
        }

        private const val ACTION = "GetArticlesRequest.Spec"
    }
}