package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetContentRequest

// TODO make special exception for whole manager exceptions
class GetContentManagerException(
    val request: GetContentRequest, override val message: String
) : Exception()
