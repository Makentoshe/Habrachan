package com.makentoshe.habrachan.application.android.common.avatar.viewmodel

data class GetAvatarViewModelRequest(val contentUrl: String) {

    val isStub: Boolean get() = Avatar.stubs.contains(contentUrl)

    object Avatar {
        const val stubMiddle = "https://habr.com/images/avatars/stub-user-middle.gif"
        const val stubSmall = "https://habr.com/images/avatars/stub-user-small.gif"
        val stubs = arrayOf(stubMiddle, stubSmall)
    }
}