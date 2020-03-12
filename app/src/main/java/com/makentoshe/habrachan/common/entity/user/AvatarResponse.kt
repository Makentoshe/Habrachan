package com.makentoshe.habrachan.common.entity.user

sealed class AvatarResponse {

    class Success(val bytes: ByteArray, val isStub: Boolean): AvatarResponse()

    class Error(val message: String): AvatarResponse()
}