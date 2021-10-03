package com.makentoshe.habrachan.application.android

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.makentoshe.habrachan.application.core.arena.ArenaException
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.GetContentException
import com.makentoshe.habrachan.network.exception.GetUserDeserializerException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

interface ExceptionHandler {

    /** Consumes [exception] and returns an [Entry] */
    fun handleException(exception: Throwable?): Entry

    /** [title] describes a main error cause and [message] is just a description */
    data class Entry(val title: String, val message: String)
}

// TODO rework exception handler
class ExceptionHandlerImpl(private val context: Context) : ExceptionHandler {

    override fun handleException(exception: Throwable?): ExceptionHandler.Entry = when (exception) {
        is ArenaStorageException -> handleArenaStorageException(exception)
        is GetContentException -> handleContentManagerException(exception)
        is ArenaException -> handleArenaException(exception)
        else -> handleUnknownException(exception)
    }

    private fun handleArenaStorageException(exception: ArenaStorageException) = when (val cause = exception.cause) {
        is SSLPeerUnverifiedException -> handleArenaStorageSSlPeerUnverifiedException(cause)
        is UnknownHostException -> handleUnknownHostException(cause)
        is SSLHandshakeException -> handleSSLHandshakeArenaStorageException(cause)
        else -> handleUnknownArenaStorageException(exception)
    }

    /** internet connection disabled */
    private fun handleUnknownHostException(exception: UnknownHostException): ExceptionHandler.Entry {
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

    private fun handleContentManagerException(exception: GetContentException) = when (val cause = exception.cause) {
        is SSLPeerUnverifiedException -> handleArenaStorageSSlPeerUnverifiedException(cause)
        is UnknownHostException -> handleUnknownHostException(cause)
        is SSLHandshakeException -> handleSSLHandshakeArenaStorageException(cause)
        else -> handleUnknownException(exception)
    }

    private fun handleArenaException(exception: ArenaException) = when(val cause = exception.sourceException) {
        is UnknownHostException -> handleUnknownHostException(cause)
        is GetUserDeserializerException -> handleGetUserDeserializerException(cause)
        else -> handleUnknownException(cause)
    }

    private fun handleGetUserDeserializerException(exception: GetUserDeserializerException): ExceptionHandler.Entry {
        val title = context.getString(R.string.exception_handler_authorization_required)
        val description = context.getString(R.string.exception_handler_authorization_required_description)
        return ExceptionHandler.Entry(title, description)
    }

    @SuppressLint("LongLogTag")
    private fun handleUnknownException(exception: Throwable?): ExceptionHandler.Entry {
        Log.e("ExceptionHandler#Unknown", exception.toString())

        val title = context.getString(R.string.exception_handler_unknown)
        val description = exception.toString()
        return ExceptionHandler.Entry(title, description)
    }
}

data class ExceptionViewHolder(val root: View) {
    val titleView: TextView = root.findViewById(R.id.fragment_article_exception_title)
    val messageView: TextView = root.findViewById(R.id.layout_exception_message)
    val retryButton: Button = root.findViewById(R.id.layout_exception_retry)
}

class ExceptionController(private val holder: ExceptionViewHolder) {

    fun render(entry: ExceptionHandler.Entry) {
        holder.root.visibility = View.VISIBLE
        holder.titleView.text = entry.title
        holder.messageView.text = entry.message
    }

    fun hide() = holder.root.setVisibility(View.GONE)

    fun setRetryButton(listener: (View) -> Unit) {
        holder.retryButton.setOnClickListener(listener)
    }
}