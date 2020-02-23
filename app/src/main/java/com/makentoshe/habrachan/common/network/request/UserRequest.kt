package com.makentoshe.habrachan.common.network.request

data class UserRequest(val clientKey: String, val apiKey: String, val tokenKey: String?, val name: String)