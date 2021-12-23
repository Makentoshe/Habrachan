package com.makentoshe.habrachan.network.content.get

import com.makentoshe.habrachan.entity.content.get.NetworkContent

data class GetContentResponse(val request: GetContentRequest, val content: NetworkContent)