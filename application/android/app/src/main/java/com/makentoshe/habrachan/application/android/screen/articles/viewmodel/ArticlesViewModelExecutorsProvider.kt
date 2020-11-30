package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import java.util.concurrent.Executor

interface ArticlesViewModelExecutorsProvider {
    val fetchExecutor: Executor
    val notifyExecutor: Executor
}

