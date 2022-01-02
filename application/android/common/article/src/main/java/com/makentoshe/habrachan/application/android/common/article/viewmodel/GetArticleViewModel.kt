package com.makentoshe.habrachan.application.android.common.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.article.R
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArena
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArenaRequest
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias GetArticleViewModelFlow = Flow<GetArticleViewModelFlowResponse>

typealias GetArticleViewModelFlowResponse = FlowArenaResponse<GetArticleViewModelResponse, ExceptionEntry<*>>

class GetArticleViewModel(
    private val stringsProvider: BundledStringsProvider,
    private val userSessionProvider: AndroidUserSessionProvider,
    private val getArticleArena: GetArticleArena,
    initialGetArticleViewModelRequestOption: Option2<GetArticleViewModelRequest>,
) : ViewModel() {

    companion object : Analytics(LogAnalytic())

    private val internalChannel = Channel<GetArticleViewModelRequest>()
    val channel: SendChannel<GetArticleViewModelRequest> get() = internalChannel

    private val internalModel = MutableSharedFlow<GetArticleViewModelFlowResponse>(replay = 1)
    val model: GetArticleViewModelFlow get() = internalModel

    init {
        capture(analyticEvent { "Initialized" })
        initialGetArticleViewModelRequestOption.fold({}) { viewModelRequest ->
            capture(analyticEvent { "Send initial $viewModelRequest" })
            viewModelScope.launch(Dispatchers.IO) {
                internalChannel.send(viewModelRequest)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            internalChannel.consumeEach { onConsumeViewModelRequest(it) }
        }
    }

    private suspend fun onConsumeViewModelRequest(request: GetArticleViewModelRequest) {
        val parameters = userSessionProvider.get()?.toRequestParameters()
            ?: return internalModel.emit(failureFlowDueUserSession())

        getArticleArena.suspendFlowFetch(GetArticleArenaRequest(request.articleId, parameters)).collect {
            internalModel.emit(it.bimap({ GetArticleViewModelResponse(request, it.article) }, { arenaExceptionEntry(it) }))
        }
    }

    private fun failureFlowDueUserSession(): GetArticleViewModelFlowResponse {
        val title = stringsProvider.getString(R.string.exception_handler_user_session_title)
        val message = stringsProvider.getString(R.string.exception_handler_user_session_message)
        return FlowArenaResponse(false, Either2.Right(ExceptionEntry(title, message, Throwable())))
    }

    private fun arenaExceptionEntry(throwable: Throwable): ExceptionEntry<*> {
        return when (val exception = throwable.cause) {
            is EmptyArenaStorageException -> this.exceptionEntry(exception)
            else -> exceptionEntry(stringsProvider, throwable)
        }
    }

    private fun exceptionEntry(throwable: EmptyArenaStorageException) = ExceptionEntry(
        title = stringsProvider.getString(R.string.exception_handler_empty_storage_title),
        message = stringsProvider.getString(R.string.exception_handler_empty_storage_message),
        throwable = throwable,
    )

    class Factory @Inject constructor(
        private val stringsProvider: BundledStringsProvider,
        private val userSessionProvider: AndroidUserSessionProvider,
        private val getArticleArena: GetArticleArena,
        private val initialGetArticleViewModelRequestOption: Option2<GetArticleViewModelRequest>,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = GetArticleViewModel(
            stringsProvider,
            userSessionProvider,
            getArticleArena,
            initialGetArticleViewModelRequestOption,
        ) as T
    }
}
