package com.makentoshe.habrachan.application.android.viewmodel

import java.util.concurrent.Executor

interface ExecutorsProvider {
    val fetchExecutor: Executor
    val notifyExecutor: Executor
}
