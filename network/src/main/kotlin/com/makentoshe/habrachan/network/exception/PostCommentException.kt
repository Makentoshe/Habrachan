package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.PostCommentRequest

abstract class PostCommentException : Throwable() {
    abstract val request: PostCommentRequest

    /** Full unparsed error */
    abstract val raw: String?
}
