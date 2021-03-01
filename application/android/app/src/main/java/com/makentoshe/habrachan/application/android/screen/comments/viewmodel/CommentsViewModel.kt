package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.lifecycle.ViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetContentRequest
import com.makentoshe.habrachan.network.response.GetContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class CommentsViewModel(
    private val avatarArena: ContentArena, private val userSession: UserSession
) : ViewModel() {

    private val avatars = HashMap<String, Flow<Result<GetContentResponse>>>()

    fun requestAvatar(avatar: String): Flow<Result<GetContentResponse>> {
        return if (avatars.containsKey(avatar)) {
            avatars[avatar] ?: requestAvatarFlow(avatar)
        } else requestAvatarFlow(avatar)
    }

    private fun requestAvatarFlow(avatar: String): Flow<Result<GetContentResponse>> = flow {
        emit(avatarArena.suspendFetch(buildImageRequest(avatar)))
    }.flowOn(Dispatchers.IO).also { flow -> avatars[avatar] = flow }

    private fun buildImageRequest(avatar: String): GetContentRequest {
        if (GetContentRequest.Avatar.stubs.contains(avatar)) {
            return avatarArena.manager.request(userSession, avatar)
        }
        return avatarArena.manager.request(userSession, "https:$avatar")
    }
}