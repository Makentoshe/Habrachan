package com.makentoshe.habrachan.network.exception

import com.google.gson.annotations.SerializedName

data class ErrorsMessage(
    @SerializedName("errors")
    val errors: String // Пользователь с такой электронной почтой или паролем не найден
)