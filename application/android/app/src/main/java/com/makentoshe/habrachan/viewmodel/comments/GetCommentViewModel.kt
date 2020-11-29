package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import io.reactivex.Observable
import io.reactivex.Observer

interface GetCommentViewModel {

    /** Requests all article comments by article id */
    val getCommentsObserver: Observer<Int>

    /** Returns all article comments */
    val getCommentsObservable: Observable<GetCommentsResponse>
}