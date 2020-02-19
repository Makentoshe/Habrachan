package com.makentoshe.habrachan.common.network.request

data class MeRequest(val clientKey: String, val tokenKey: String) {

    class Builder(private val clientKey: String, private val tokenKey: String) {
        fun build() = MeRequest(clientKey, tokenKey)
    }
}

