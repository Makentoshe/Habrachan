package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class CompaniesContribution(
    @SerializedName("companyRefs")
    val companyRefs: Any,
    @SerializedName("flows")
    val flows: Any,
    @SerializedName("hubs")
    val hubs: Any
)