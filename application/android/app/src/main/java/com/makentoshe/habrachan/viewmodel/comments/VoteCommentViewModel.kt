package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import io.reactivex.Observable
import io.reactivex.Observer

interface VoteCommentViewModel {

    /** Performs vote up request using comment id */
    val voteUpCommentObserver: Observer<Int>

    /** Returns a vote action response */
    val voteUpCommentObservable: Observable<VoteCommentResponse>

    /** Performs vote down request using comment id */
    val voteDownCommentObserver: Observer<Int>

    /** Returns a vote action response */
    val voteDownCommentObservable: Observable<VoteCommentResponse>
}