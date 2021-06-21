package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.User

data class Me(
    @SerializedName("errorMessage")
    val errorMessage: String,
    @SerializedName("karmaResetInfo")
    val karmaResetInfo: KarmaResetInfo,
    @SerializedName("notes")
    val notes: Any?, // null
    @SerializedName("user")
    val user: User
)