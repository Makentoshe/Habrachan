package com.makentoshe.habrachan.application.android.screen.articles.model

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@EpoxyModelClass(layout = R.layout.fragment_articles_footer)
abstract class FooterEpoxyModel : EpoxyModelWithHolder<FooterEpoxyModel.ViewHolder>() {

    private lateinit var compositeDisposable: CompositeDisposable

    /** Returns last after load result */
    private lateinit var afterObservable: Observable<Result<*>>

    /** Allows to retry last after load */
    private lateinit var retryObserver: Observer<Unit>

    fun disposables(disposables: CompositeDisposable) {
        compositeDisposable = disposables
    }

    fun afterObservable(observable: Observable<Result<*>>) {
        afterObservable = observable
    }

    fun retryObserver(observer: Observer<Unit>) {
        retryObserver = observer
    }

    override fun bind(holder: ViewHolder) {
        afterObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            println(it)
        }.let(compositeDisposable::add)
    }

    override fun unbind(holder: ViewHolder) {
        super.unbind(holder)
        compositeDisposable.clear()
    }

    class ViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) {
        }
    }

    class Factory {
        /**
         * [afterObservable] - calls on each after event. It can be succeed or failed
         * [retryObserver] - retries last after loading and cause [afterObservable] on finish
         */
        fun build(afterObservable: Observable<Result<*>>, retryObserver: Observer<Unit>): FooterEpoxyModel {
            val model = FooterEpoxyModel_()
            model.id("footer")
            model.disposables(CompositeDisposable())
            model.afterObservable(afterObservable)
            model.retryObserver(retryObserver)
            return model
        }
    }
}