package com.makentoshe.habrachan.network.exception

import com.google.gson.annotations.SerializedName

data class ErrorsList(
    @SerializedName("errors")
    val errors: List<Error>
)