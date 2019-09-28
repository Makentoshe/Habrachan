package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class UsersData(
    @SerializedName("pages")
    val pages: Int? = null,
    @SerializedName("users")
    val users: List<User>? = null,
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null
)