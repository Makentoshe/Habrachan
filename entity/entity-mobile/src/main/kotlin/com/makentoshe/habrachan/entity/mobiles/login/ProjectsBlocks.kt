package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class ProjectsBlocks(
    @SerializedName("blockCompany")
    val blockCompany: Any?, // null
    @SerializedName("blockName")
    val blockName: Any?, // null
    @SerializedName("itemsList")
    val itemsList: List<Any>
)