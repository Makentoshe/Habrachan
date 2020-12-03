package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import io.reactivex.Scheduler

interface SchedulersProvider {
    val ioScheduler: Scheduler
}