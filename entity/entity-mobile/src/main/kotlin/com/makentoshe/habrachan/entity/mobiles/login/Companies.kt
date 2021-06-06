package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Companies(
    @SerializedName("companiesCategories")
    val companiesCategories: List<Any>,
    @SerializedName("companiesFans")
    val companiesFans: Any,
    @SerializedName("companiesWidgets")
    val companiesWidgets: Any,
    @SerializedName("companiesWorkers")
    val companiesWorkers: Any,
    @SerializedName("companyFansLoading")
    val companyFansLoading: Boolean, // false
    @SerializedName("companyIds")
    val companyIds: Any,
    @SerializedName("companyProfiles")
    val companyProfiles: Any,
    @SerializedName("companyRefs")
    val companyRefs: Map<String, Company>,
    @SerializedName("companyTopIds")
    val companyTopIds: List<String>,
    @SerializedName("companyWorkersLoading")
    val companyWorkersLoading: Boolean, // false
    @SerializedName("isLoading")
    val isLoading: Boolean, // false
    @SerializedName("pagesCount")
    val pagesCount: Any,
    @SerializedName("route")
    val route: Any
)