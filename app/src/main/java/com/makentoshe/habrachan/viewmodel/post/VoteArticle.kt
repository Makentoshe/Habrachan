package com.makentoshe.habrachan.viewmodel.post

import io.reactivex.disposables.CompositeDisposable

class VoteArticle(private val disposables: CompositeDisposable, private val articleId: Int) {

    fun voteUp() {
        println("VoteUp")
    }

    fun voteDown() {
        println("VoteDown")
    }
}