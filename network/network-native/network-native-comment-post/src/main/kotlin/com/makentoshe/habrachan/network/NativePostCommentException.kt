package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.exception.PostCommentException

class NativePostCommentException(
    override val request: NativePostCommentRequest,
    override val raw: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null,
): PostCommentException()