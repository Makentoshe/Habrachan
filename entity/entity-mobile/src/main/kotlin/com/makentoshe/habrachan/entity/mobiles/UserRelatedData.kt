package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class UserRelatedData(
    @SerializedName("canVote")
    val canVote: Boolean, // false
    @SerializedName("isSubscribed")
    val isSubscribed: Boolean, // false
    @SerializedName("vote")
    val vote: UserVote
)