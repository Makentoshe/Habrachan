package com.makentoshe.habrachan.viewmodel.main.articles

import java.util.concurrent.Executor

interface ArticlesViewModelExecutorsProvider {
    val fetchExecutor: Executor
    val notifyExecutor: Executor
}

