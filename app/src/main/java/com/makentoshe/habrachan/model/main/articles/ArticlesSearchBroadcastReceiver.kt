package com.makentoshe.habrachan.model.main.articles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ArticlesSearchBroadcastReceiver : BroadcastReceiver() {

    private val broadcastSubject = PublishSubject.create<GetArticlesRequest.Spec>()
    val broadcastObservable: Observable<GetArticlesRequest.Spec> = broadcastSubject

    override fun onReceive(context: Context, intent: Intent) {
        val spec = intent.getStringExtra(SPEC) ?: return
        val sort = intent.getStringExtra(SORT)
        broadcastSubject.onNext(GetArticlesRequest.Spec(spec, sort))
    }

    fun registerReceiver(context: Context) {
        context.registerReceiver(this, IntentFilter(ACTION))
    }

    companion object {

        fun sendBroadcast(context: Context, spec: GetArticlesRequest.Spec) {
            val intent = Intent(ACTION).putExtra(SPEC, spec.request).putExtra(SORT, spec.sort)
            context.sendBroadcast(intent)
        }

        private const val ACTION = "GetArticlesRequest.Spec"
        private const val SPEC = "spec"
        private const val SORT = "sort"
    }
}