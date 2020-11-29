package com.makentoshe.habrachan.viewmodel.user.articles

import java.util.concurrent.Executor

interface UserArticlesViewModelExecutorsProvider {
    val fetchExecutor: Executor
    val notifyExecutor: Executor
}

