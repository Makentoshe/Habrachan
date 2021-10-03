package com.makentoshe.habrachan.application.android.exception

import android.content.Context
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ExceptionHandler(private val context: Context) {

    fun handle(throwable: Throwable): ExceptionEntry = when(throwable) {
        is UnknownHostException -> handle(throwable)
        is SSLHandshakeException -> handle(throwable)
        else -> unknownHandle(throwable)
    }

    fun handle(exception: SSLHandshakeException): ExceptionEntry {
        val title = context.getString(R.string.exception_handler_network_title)
        return ExceptionEntry(title, exception.toString())
    }

    fun handle(exception: UnknownHostException): ExceptionEntry {
        val title = context.getString(R.string.exception_handler_network_unknown_host_title)
        val message = context.getString(R.string.exception_handler_network_unknown_host_message)
        return ExceptionEntry(title,  message)
    }

    private fun unknownHandle(exception: Throwable): ExceptionEntry {
        val title = context.getString(R.string.exception_handler_unknown_title)
        val message = exception.toString()
        return ExceptionEntry(title, message)
    }
}

