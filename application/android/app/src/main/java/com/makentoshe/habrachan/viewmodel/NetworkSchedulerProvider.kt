package com.makentoshe.habrachan.viewmodel

import io.reactivex.Scheduler

/** Provides scheduler to ViewModels for performing something in network thread */
interface NetworkSchedulerProvider {
    /** Should not be the main thread */
    val networkScheduler: Scheduler
}