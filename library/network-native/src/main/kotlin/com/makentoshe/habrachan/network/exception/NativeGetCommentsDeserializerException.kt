package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.NativeGetCommentsRequest

class NativeGetCommentsDeserializerException(
    override val request: NativeGetCommentsRequest, override val raw: String
) : GetCommentsDeserializerException()