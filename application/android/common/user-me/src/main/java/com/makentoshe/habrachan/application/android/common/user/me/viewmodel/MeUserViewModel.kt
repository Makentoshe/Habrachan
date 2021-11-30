package com.makentoshe.habrachan.application.android.common.user.me.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.FlowArenaResponse
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaRequest
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MeUserViewModel(
    private val userSessionProvider: AndroidUserSessionProvider,
    private val meUserArena: MeUserArena,
    initialMeUserRequestOption: Option2<MeUserViewModelRequest>,
) : ViewModel() {

    private val internalChannel = Channel<MeUserViewModelRequest>()
    val channel: SendChannel<MeUserViewModelRequest> get() = internalChannel

    @Suppress("EXPERIMENTAL_API_USAGE")
    val model = internalChannel.receiveAsFlow().map { viewModelRequest ->
        val parameters = userSessionProvider.get()?.toRequestParameters()
            ?: return@map failureFlowDueUserSession()

        meUserArena.suspendFlowFetch(MeUserArenaRequest(parameters))
    }.flatMapConcat { it }.flowOn(Dispatchers.IO)

    init {
        capture(analyticEvent { "Initialized" })
        initialMeUserRequestOption.onNotEmpty { meUserViewModelRequest ->
            capture(analyticEvent { "Send initial $meUserViewModelRequest" })
            viewModelScope.launch(Dispatchers.IO) {
                channel.send(meUserViewModelRequest)
            }
        }
    }

    private fun failureFlowDueUserSession() = flow<FlowArenaResponse<Either2<Nothing, ArenaException>>> {
        FlowArenaResponse(false, Either2.Right(ArenaException()))
    }

    class Factory @Inject constructor(
        private val userSessionProvider: AndroidUserSessionProvider,
        private val meUserArena: MeUserArena,
        private val initialMeUserRequestOption: Option2<MeUserViewModelRequest>,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MeUserViewModel(userSessionProvider, meUserArena, initialMeUserRequestOption) as T
        }
    }

    companion object : Analytics(LogAnalytic())
}

