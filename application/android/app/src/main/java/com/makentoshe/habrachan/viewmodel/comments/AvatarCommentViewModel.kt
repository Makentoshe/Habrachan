package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.common.network.response.ImageResponse
import io.reactivex.Observable

interface AvatarCommentViewModel {

    /**
     * Performs an avatar image request.
     * Returns an observable to image response by url.
     */
    fun getAvatarObservable(url: String): Observable<ImageResponse>
}