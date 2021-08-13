package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.Request

abstract class NativeRequest: Request {
    override val domain: String = "https://habr.com/"
}