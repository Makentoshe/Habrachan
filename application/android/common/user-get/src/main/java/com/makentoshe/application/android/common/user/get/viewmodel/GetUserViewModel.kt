package com.makentoshe.application.android.common.user.get.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.application.android.common.user.get.R
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArena
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.get.UserFromArena
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetUserViewModelRequest(val userLogin: UserLogin)
data class GetUserViewModelResponse(val request: GetUserViewModelRequest, val user: UserFromArena)

class GetUserViewModel(
    private val stringsProvider: BundledStringsProvider,
    private val userSessionProvider: AndroidUserSessionProvider,
    private val getUserArena: GetUserArena,
    initialMeUserRequestOption: Option2<GetUserViewModelRequest>,
) : ViewModel() {

    private val internalChannel = Channel<GetUserViewModelRequest>()
    val channel: SendChannel<GetUserViewModelRequest> get() = internalChannel

    @Suppress("EXPERIMENTAL_API_USAGE")
    val model = internalChannel.receiveAsFlow().onEach {
        capture(analyticEvent { "Send $it" })
    }.map { viewModelRequest ->
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
        request: GetUserViewModelRequest,
        parameters: AdditionalRequestParameters,
    ) = getUserArena.suspendFlowFetch(GetUserArenaRequest(request.userLogin, parameters)).map { response ->
        FlowArenaResponse(response.loading, response.content.bimap({
            GetUserViewModelResponse(request, it.user)
        }, { throwable ->
            arenaExceptionEntry(throwable)
        }))
    }

    private fun failureFlowDueUserSession() = flow<FlowArenaResponse<GetUserViewModelResponse, ExceptionEntry<*>>> {
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
        private val getUserArena: GetUserArena,
        private val initialGetUserRequestOption: Option2<GetUserViewModelRequest>,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetUserViewModel(stringsProvider, userSessionProvider, getUserArena, initialGetUserRequestOption) as T
        }
    }

    companion object : Analytics(LogAnalytic())
}

