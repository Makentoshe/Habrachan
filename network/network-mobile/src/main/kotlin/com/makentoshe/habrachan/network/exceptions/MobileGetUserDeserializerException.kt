package com.makentoshe.habrachan.network.exceptions

import com.makentoshe.habrachan.network.exception.GetUserDeserializerException
import com.makentoshe.habrachan.network.request.GetUserRequest

class MobileGetUserDeserializerException(
    override val request: GetUserRequest, override val raw: String
) : GetUserDeserializerException()