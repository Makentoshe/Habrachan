package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class RelatedData(
    @SerializedName("bookmarked")
    val bookmarked: Boolean, // false
    @SerializedName("canViewVotes")
    val canViewVotes: Boolean, // false
    @SerializedName("canVote")
    val canVote: Boolean, // true
    @SerializedName("unreadCommentsCount")
    val unreadCommentsCount: Int, // 1
    @SerializedName("vote")
    val vote: Vote,
    @SerializedName("сanComment") // Attention: Cyrillic symbols
    val canComment: Boolean, // true
    @SerializedName("сanEdit") // Attention: Cyrillic symbols
    val canEdit: Boolean // false
)