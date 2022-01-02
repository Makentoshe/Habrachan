@file:OptIn(ExperimentalCoroutinesApi::class)

package com.makentoshe.habrachan.application.android.common.avatar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.avatar.R
import com.makentoshe.habrachan.application.android.common.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.common.exception.exceptionEntry
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.content.GetContentArena
import com.makentoshe.habrachan.application.common.arena.content.GetContentArenaRequest
import com.makentoshe.habrachan.application.common.arena.content.GetContentArenaResponse
import com.makentoshe.habrachan.functional.Either2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

typealias GetAvatarViewModelFlow = Flow<GetAvatarViewModelFlowResult>

typealias GetAvatarViewModelFlowResult = FlowArenaResponse<GetAvatarViewModelResponse, ExceptionEntry<*>>

class GetAvatarViewModel(
    private val stringsProvider: BundledStringsProvider,
    private val userSessionProvider: AndroidUserSessionProvider,
    private val avatarArena: GetContentArena,
) : ViewModel() {

    /** Contains an avatars */
    private val avatars = HashMap<GetAvatarViewModelRequest, GetAvatarViewModelFlow>()

    suspend fun requestAvatar(avatar: GetAvatarViewModelRequest): GetAvatarViewModelFlow {
        return if (avatars.containsKey(avatar)) {
            avatars[avatar] ?: requestAvatarFlow(avatar)
        } else requestAvatarFlow(avatar)
    }

    private suspend fun requestAvatarFlow(request: GetAvatarViewModelRequest): GetAvatarViewModelFlow {
        val parameters = userSessionProvider.get()?.toRequestParameters() ?: return failureFlowDueUserSession()

        return avatarArena.suspendFlowFetch(GetContentArenaRequest(request.contentUrl, parameters)).map {
            it.toGetAvatarViewModelResponse(request)
        }.flowOn(Dispatchers.IO).also { avatars[request] = it }
    }

    private fun failureFlowDueUserSession() = flow<GetAvatarViewModelFlowResult> {
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

    private fun FlowArenaResponse<GetContentArenaResponse, ArenaException>.toGetAvatarViewModelResponse(request: GetAvatarViewModelRequest) =
        FlowArenaResponse(loading, content.mapLeft { GetAvatarViewModelResponse(request, it.content) }.mapRight(::arenaExceptionEntry))

    class Factory @Inject constructor(
        private val stringsProvider: BundledStringsProvider,
        private val userSessionProvider: AndroidUserSessionProvider,
        private val avatarArena: GetContentArena,
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetAvatarViewModel(stringsProvider, userSessionProvider, avatarArena) as T
        }
    }

    companion object : Analytics(LogAnalytic())
}
