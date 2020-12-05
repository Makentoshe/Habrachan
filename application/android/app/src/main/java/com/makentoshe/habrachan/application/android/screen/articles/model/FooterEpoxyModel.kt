package com.makentoshe.habrachan.application.android.screen.articles.model

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.ExceptionHandler
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

    private lateinit var exceptionHandler: ExceptionHandler

    fun disposables(disposables: CompositeDisposable) {
        compositeDisposable = disposables
    }

    fun afterObservable(observable: Observable<Result<*>>) {
        afterObservable = observable
    }

    fun retryObserver(observer: Observer<Unit>) {
        retryObserver = observer
    }

    fun exceptionHandler(exceptionHandler: ExceptionHandler) {
        this.exceptionHandler = exceptionHandler
    }

    // TODO provide exception handler
    override fun bind(holder: ViewHolder) {
        holder.progress.visibility = View.VISIBLE
        holder.title.visibility = View.GONE
        holder.message.visibility = View.GONE
        holder.retry.visibility = View.GONE

        holder.retry.setOnClickListener { onRetry(holder) }

        afterObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            it.onFailure { throwable -> onFailure(holder, throwable) }
        }.let(compositeDisposable::add)
    }

    private fun onFailure(holder: ViewHolder, throwable: Throwable) {
        holder.retry.visibility = View.VISIBLE
        holder.progress.visibility = View.GONE

        val entry = exceptionHandler.handleException(throwable)

        holder.title.visibility = View.VISIBLE
        holder.title.text = entry.title

        holder.message.visibility = View.VISIBLE
        holder.message.text = entry.message
    }

    private fun onRetry(holder: ViewHolder) {
        holder.progress.visibility = View.VISIBLE
        holder.title.visibility = View.GONE
        holder.message.visibility = View.GONE
        holder.retry.visibility = View.GONE

        retryObserver.onNext(Unit)
    }

    override fun unbind(holder: ViewHolder) {
        super.unbind(holder)
        compositeDisposable.clear()
    }

    class ViewHolder : EpoxyHolder() {
        lateinit var progress: ProgressBar
        lateinit var title: TextView
        lateinit var message: TextView
        lateinit var retry: Button

        override fun bindView(itemView: View) {
            progress = itemView.findViewById(R.id.fragment_articles_footer_progress)
            title = itemView.findViewById(R.id.fragment_articles_footer_title)
            message = itemView.findViewById(R.id.fragment_articles_footer_message)
            retry = itemView.findViewById(R.id.fragment_articles_footer_retry)
        }
    }

    class Factory(private val exceptionHandler: ExceptionHandler) {

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
            model.exceptionHandler(exceptionHandler)
            return model
        }
    }
}