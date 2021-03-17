package com.makentoshe.habrachan.network.request

abstract class NativeRequest: Request {
    override val domain: String = "https://habr.com/"
}