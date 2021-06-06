package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("authorFollowed")
    val authorFollowed: Any,
    @SerializedName("authorFollowedLoading")
    val authorFollowedLoading: Boolean, // false
    @SerializedName("authorFollowers")
    val authorFollowers: Any,
    @SerializedName("authorFollowersLoading")
    val authorFollowersLoading: Boolean, // false
    @SerializedName("authorIds")
    val authorIds: Any,
    @SerializedName("authorProfiles")
    val authorProfiles: Any,
    @SerializedName("authorRefs")
    val authorRefs: Any,
    @SerializedName("isLoading")
    val isLoading: Boolean, // false
    @SerializedName("karmaStats")
    val karmaStats: List<Any>,
    @SerializedName("pagesCount")
    val pagesCount: Any,
    @SerializedName("route")
    val route: Any,
    @SerializedName("statistics")
    val statistics: Any? // null
)