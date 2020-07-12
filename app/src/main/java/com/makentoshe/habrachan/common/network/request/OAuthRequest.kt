package com.makentoshe.habrachan.common.network.request

import io.reactivex.subjects.BehaviorSubject

data class OAuthRequest(
    val clientId: String,
    val socialType: String,
    val hostUrl: String,
    val responseType: String = "token",
    val redirectUri: String = "http://cleverpumpkin.ru"
) {
    /** Request OAuth action for url */
    val requestSubject = BehaviorSubject.create<String>()

    /** Callback for OAuth requested action */
    val responseSubject = BehaviorSubject.create<String>()
}
