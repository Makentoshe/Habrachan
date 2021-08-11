package com.makentoshe.habrachan.application.android.screen.content.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.GetContentResponse
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

// TODO add userSessionProvider
@Suppress("EXPERIMENTAL_API_USAGE")
class ContentViewModel(
    private val contentArena: ContentArena, private val userSession: UserSession
) : ViewModel() {

    /**
     * Sending to this channel cause image loading process.
     * The result will be placed in [response] and in [content] in parallel.
     */
    val sourceChannel = Channel<ContentSpec>()

    /** Channel contains image loading result for future using */
    val response = ConflatedBroadcastChannel<Result<GetContentResponse>>()

    /**
     * Flow using for receiving image loading result for the first time
     * for the displaying in the ui.
     * We does not using response.asFlow because there are some troubles with it
     */
    val content = sourceChannel.receiveAsFlow().map { spec ->
        contentArena.suspendFetch(contentArena.manager.request(userSession, spec.source))
    }.onEach(response::send).flowOn(Dispatchers.IO)

    class Factory(
        private val contentArena: ContentArena,
        private val userSession: UserSession
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ContentViewModel(contentArena, userSession) as T
        }
    }

    data class ContentSpec(val source: String)
}