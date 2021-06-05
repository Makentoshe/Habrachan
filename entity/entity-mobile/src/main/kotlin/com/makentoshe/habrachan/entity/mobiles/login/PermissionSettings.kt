package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class PermissionSettings(
    @SerializedName("canAddComplaints")
    val canAddComplaints: Boolean, // false
    @SerializedName("canCreateVoices")
    val canCreateVoices: Boolean // false
)