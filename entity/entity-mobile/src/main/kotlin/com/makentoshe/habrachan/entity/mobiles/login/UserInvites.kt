package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class UserInvites(
    @SerializedName("availableInvites")
    val availableInvites: Int, // 0
    @SerializedName("unusedInvitesIds")
    val unusedInvitesIds: List<Any>,
    @SerializedName("unusedInvitesPagesCount")
    val unusedInvitesPagesCount: Int, // 0
    @SerializedName("unusedInvitesRefs")
    val unusedInvitesRefs: Any,
    @SerializedName("usedInvitesIds")
    val usedInvitesIds: List<Any>,
    @SerializedName("usedInvitesPagesCount")
    val usedInvitesPagesCount: Int, // 0
    @SerializedName("usedInvitesRefs")
    val usedInvitesRefs: Any
)