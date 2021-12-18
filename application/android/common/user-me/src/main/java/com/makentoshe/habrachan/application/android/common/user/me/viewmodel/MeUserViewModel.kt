package com.makentoshe.habrachan.application.android.common.user.me.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.user.me.R
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserFromArena
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MeUserViewModelRequest
data class MeUserViewModelResponse(val request: MeUserViewModelRequest, val me: MeUserFromArena)

class MeUserViewModel(
    private val stringsProvider: BundledStringsProvider,
    private val userSessionProvider: AndroidUserSessionProvider,
    private val meUserArena: MeUserArena,
    initialMeUserRequestOption: Option2<MeUserViewModelRequest>,
) : ViewModel() {

    private val internalChannel = Channel<MeUserViewModelRequest>()
    val channel: SendChannel<MeUserViewModelRequest> get() = internalChannel

    @Suppress("EXPERIMENTAL_API_USAGE")
    val model = internalChannel.receiveAsFlow().map { viewModelRequest ->
        val parameters = userSessionProvider.get()?.toRequestParameters() ?: return@map failureFlowDueUserSession()
        return@map successFlowViaMeUserArena(viewModelRequest, parameters)
    }.flowOn(Dispatchers.IO).flatMapMerge { it }

    init {
        capture(analyticEvent { "Initialized" })
        initialMeUserRequestOption.onNotEmpty { meUserViewModelRequest ->
            capture(analyticEvent { "Send initial $meUserViewModelRequest" })
            viewModelScope.launch(Dispatchers.IO) {
                channel.send(meUserViewModelRequest)
            }
        }
    }

    private suspend fun successFlowViaMeUserArena(
        request: MeUserViewModelRequest,
        parameters: AdditionalRequestParameters,
    ) = meUserArena.suspendFlowFetch(MeUserArenaRequest(parameters)).map { response ->
        FlowArenaResponse(response.loading, response.content.bimap({
            MeUserViewModelResponse(request, it.me)
        }, { throwable ->
            arenaExceptionEntry(throwable)
        }))
    }

    private fun failureFlowDueUserSession() = flow<FlowArenaResponse<MeUserViewModelResponse, ExceptionEntry<*>>> {
        val title = stringsProvider.getString(R.string.exception_handler_user_session_title)
        val message = stringsProvider.getString(R.string.exception_handler_user_session_message)
        FlowArenaResponse(false, Either2.Right(ExceptionEntry(title, message, Throwable())))
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
        throwable = throwable
    )


    class Factory @Inject constructor(
        private val stringsProvider: BundledStringsProvider,
        private val userSessionProvider: AndroidUserSessionProvider,
        private val meUserArena: MeUserArena,
        private val initialMeUserRequestOption: Option2<MeUserViewModelRequest>,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MeUserViewModel(stringsProvider, userSessionProvider, meUserArena, initialMeUserRequestOption) as T
        }
    }

    companion object : Analytics(LogAnalytic())
}

