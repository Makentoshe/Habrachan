package com.makentoshe.habrachan.application.android.screen.content.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.core.arena.image.ImageArena
import com.makentoshe.habrachan.network.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class ContentViewModel(private val imageArena: ImageArena) : ViewModel(){

    val sourceChannel = Channel<ImageSpec>()

    val content = sourceChannel.receiveAsFlow().map { spec ->
        imageArena.suspendFetch(ImageRequest(spec.source))
    }.flowOn(Dispatchers.IO)

    class Factory(
        private val imageArena: ImageArena
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ContentViewModel(imageArena) as T
        }
    }

    data class ImageSpec(val source: String)
}