package com.makentoshe.habrachan.viewmodel.main.articles

import io.reactivex.Scheduler

interface ArticlesViewModelSchedulersProvider {
    val ioScheduler: Scheduler
}