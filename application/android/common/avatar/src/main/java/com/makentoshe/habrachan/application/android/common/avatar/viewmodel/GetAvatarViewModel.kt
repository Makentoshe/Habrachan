package com.makentoshe.habrachan.application.android.common.avatar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.common.arena.content.ContentArena
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.GetContentRequest
import com.makentoshe.habrachan.network.GetContentResponse
import com.makentoshe.habrachan.network.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAvatarViewModel(
    private val userSession: UserSession,
    private val avatarArena: ContentArena,
) : ViewModel() {

    /** Contains an avatars */
    private val avatars = HashMap<GetAvatarSpec, Flow<Result<GetContentResponse>>>()

    fun requestAvatar(avatar: GetAvatarSpec): Flow<Result<GetContentResponse>> {
        return if (avatars.containsKey(avatar)) {
            avatars[avatar] ?: requestAvatarFlow(avatar)
        } else requestAvatarFlow(avatar)
    }

    private fun requestAvatarFlow(avatar: GetAvatarSpec): Flow<Result<GetContentResponse>> = flow {
        emit(avatarArena.suspendFetch(buildImageRequest(avatar)))
    }.flowOn(Dispatchers.IO).also { flow -> avatars[avatar] = flow }

    private fun buildImageRequest(avatar: GetAvatarSpec): GetContentRequest {
        if (GetContentRequest.Avatar.stubs.contains(avatar.url)) {
            return avatarArena.request(userSession, avatar.url)
        }
        return avatarArena.request(userSession, "https:${avatar.url}")
    }

    class Factory @Inject constructor(
        private val userSession: UserSession,
        private val avatarArena: ContentArena,
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GetAvatarViewModel(userSession, avatarArena) as T
        }
    }
}
