package com.makentoshe.habrachan.viewmodel.user.articles

import io.reactivex.Scheduler

interface UserArticlesViewModelSchedulersProvider {
    val ioScheduler: Scheduler
}