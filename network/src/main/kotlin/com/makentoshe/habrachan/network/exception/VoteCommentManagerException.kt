package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.VoteCommentRequest2

abstract class VoteCommentManagerException : Throwable() {
    abstract val request: VoteCommentRequest2

    /** Full unparsed error */
    abstract val raw: String
}
