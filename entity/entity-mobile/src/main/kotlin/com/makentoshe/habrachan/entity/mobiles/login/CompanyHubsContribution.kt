package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class CompanyHubsContribution(
    @SerializedName("contributionRefs")
    val contributionRefs: ContributionRefs
)