package com.makentoshe.habrachan.application.android.common.exception

import android.content.Context
import com.makentoshe.habrachan.application.android.common.R
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import java.net.UnknownHostException

data class ExceptionEntry<T : Throwable>(val title: String, val message: String, val throwable: T)

fun exceptionEntry(context: Context, unknownHostException: UnknownHostException) = ExceptionEntry(
    title = context.getString(R.string.exception_handler_network_unknown_host_title),
    message = context.getString(R.string.exception_handler_network_unknown_host_message),
    throwable = unknownHostException
)

fun exceptionEntry(context: Context, throwable: Throwable?) = ExceptionEntry(
    title = context.getString(R.string.exception_handler_unknown_title),
    message = context.getString(R.string.exception_handler_unknown_message),
    throwable = throwable ?: NullPointerException("Throwable is null")
)

fun exceptionEntry(stringsProvider: BundledStringsProvider, throwable: Throwable?) = ExceptionEntry(
    title = stringsProvider.getString(R.string.exception_handler_unknown_title),
    message = stringsProvider.getString(R.string.exception_handler_unknown_message),
    throwable = throwable ?: NullPointerException("Throwable is null")
)
