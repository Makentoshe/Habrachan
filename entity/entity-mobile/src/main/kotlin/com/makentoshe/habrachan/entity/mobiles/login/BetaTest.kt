package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class BetaTest(
    @SerializedName("announcementCards")
    val announcementCards: Any?, // null
    @SerializedName("announcementCommentThreads")
    val announcementCommentThreads: Any,
    @SerializedName("announcementCommentingStatuses")
    val announcementCommentingStatuses: Any,
    @SerializedName("announcementComments")
    val announcementComments: Any,
    @SerializedName("announcements")
    val announcements: Any,
    @SerializedName("archivedList")
    val archivedList: List<Any>,
    @SerializedName("currentAnnouncement")
    val currentAnnouncement: Any? // null
)