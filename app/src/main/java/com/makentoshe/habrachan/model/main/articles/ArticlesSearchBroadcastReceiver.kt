package com.makentoshe.habrachan.model.main.articles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.makentoshe.habrachan.common.entity.session.UserSession
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ArticlesSearchBroadcastReceiver : BroadcastReceiver() {

    private val broadcastSubject = PublishSubject.create<UserSession.ArticlesRequestSpec>()
    val broadcastObservable: Observable<UserSession.ArticlesRequestSpec> = broadcastSubject

    override fun onReceive(context: Context, intent: Intent) {
        val spec = intent.getStringExtra(SPEC) ?: return
        broadcastSubject.onNext(UserSession.ArticlesRequestSpec(spec))
    }

    fun registerReceiver(context: Context) {
        context.registerReceiver(this, IntentFilter(ACTION))
    }

    companion object {

        fun sendBroadcast(context: Context, spec: UserSession.ArticlesRequestSpec) {
            val intent = Intent(ACTION).putExtra(SPEC, spec.request)
            context.sendBroadcast(intent)
        }

        private const val ACTION = "ArticlesRequestSpec"
        private const val SPEC = "spec"
    }
}