package com.makentoshe.habrachan.common.network.request

data class UserRequest(val client: String, val api: String, val token: String?, val name: String)