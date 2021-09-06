package com.makentoshe.habrachan.application.android.common

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Flow<Result<T>>.collectResult(lifecycleScope: CoroutineScope, success: (T) -> Unit, failure: (Throwable) -> Unit) {
    lifecycleScope.launch { collectLatest { it.fold(success, failure) } }
}
