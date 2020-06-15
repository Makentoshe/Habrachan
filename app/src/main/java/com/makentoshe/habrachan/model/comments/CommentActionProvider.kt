package com.makentoshe.habrachan.model.comments

import com.makentoshe.habrachan.common.entity.comment.Comment
import io.reactivex.Observer

interface CommentActionProvider {

    /** Opens comment's owner user screen */
    val inspectUserObserver: Observer<Comment>

    /** Increase comment score number */
    val voteUpCommentObserver: Observer<Comment>

    /** Decrease comment score number  */
    val voteDownCommentObserver: Observer<Comment>

    /** Opens new screen with comments line from root to current */
    val replyCommentObserver: Observer<Comment>
}