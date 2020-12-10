package com.makentoshe.habrachan.application.android

import android.content.Context
import android.util.Log
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

interface ExceptionHandler {

    /** Consumes [exception] and returns an [Entry] */
    fun handleException(exception: Throwable?): Entry

    /** [title] describes a main error cause and [message] is just a description */
    data class Entry(val title: String, val message: String)
}

class ExceptionHandlerImpl(private val context: Context) : ExceptionHandler {

    override fun handleException(exception: Throwable?): ExceptionHandler.Entry = when (exception) {
        is ArenaStorageException -> handleArenaStorageException(exception)
        else -> handleUnknownException(exception)
    }

    private fun handleArenaStorageException(exception: ArenaStorageException) = when (val cause = exception.cause) {
        is SSLPeerUnverifiedException -> handleArenaStorageSSlPeerUnverifiedException(cause)
        is UnknownHostException -> handleArenaStorageUnknownHostException(cause)
        is SSLHandshakeException -> handleSSLHandshakeArenaStorageException(cause)
        else -> handleUnknownArenaStorageException(exception)
    }

    /** internet connection disabled */
    private fun handleArenaStorageUnknownHostException(exception: UnknownHostException): ExceptionHandler.Entry {
        val title = context.getString(R.string.exception_handler_network)
        val description = context.getString(R.string.exception_handler_network_unknown_host)
        return ExceptionHandler.Entry(title, description)
    }

    /** provider blocks the host */
    private fun handleArenaStorageSSlPeerUnverifiedException(exception: SSLPeerUnverifiedException): ExceptionHandler.Entry {
        val title = context.getString(R.string.exception_handler_network)
        val description = context.getString(R.string.exception_handler_network_sslpeer_unverified)
        return ExceptionHandler.Entry(title, description)
    }

    // TODO
    private fun handleSSLHandshakeArenaStorageException(exception: SSLHandshakeException): ExceptionHandler.Entry {
        val title = context.getString(R.string.exception_handler_network)
        return ExceptionHandler.Entry(title, exception.toString())
    }

    private fun handleUnknownArenaStorageException(exception: ArenaStorageException): ExceptionHandler.Entry {
        exception.printStackTrace()

        val title = context.getString(R.string.exception_handler_unknown_cache)
        val description = exception.toString()
        return ExceptionHandler.Entry(title, description)
    }

    private fun handleUnknownException(exception: Throwable?): ExceptionHandler.Entry {
        Log.e("ExceptionHandler#Unknown", exception.toString())

        val title = context.getString(R.string.exception_handler_unknown)
        val description = exception.toString()
        return ExceptionHandler.Entry(title, description)
    }
}