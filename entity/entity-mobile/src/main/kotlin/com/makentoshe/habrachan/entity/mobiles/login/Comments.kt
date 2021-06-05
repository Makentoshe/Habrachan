package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Comments(
    @SerializedName("commentAccess")
    val commentAccess: Any?,
    @SerializedName("commentWithOpenedForm")
    val commentWithOpenedForm: Any?, // null
    @SerializedName("commentsListV2")
    val commentsListV2: Any?,
    @SerializedName("isFormDisabled")
    val isFormDisabled: Boolean, // false
    @SerializedName("pageArticleComments")
    val pageArticleComments: PageArticleComments,
    @SerializedName("pagesCount")
    val pagesCount: Any?, // null
    @SerializedName("previewComment")
    val previewComment: Any?, // null
    @SerializedName("scrollParents")
    val scrollParents: Any?,
    @SerializedName("threads")
    val threads: List<Any>
)