package com.makentoshe.habrachan.network.request

abstract class MobileRequest: Request {
    override val domain: String = "https://m.habr.com/"
}