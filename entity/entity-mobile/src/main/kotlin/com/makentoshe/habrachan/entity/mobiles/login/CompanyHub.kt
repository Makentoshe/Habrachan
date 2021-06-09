package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class CompanyHub(
    @SerializedName("alias")
    val alias: String, // webdev
    @SerializedName("id")
    val id: String, // 91
    @SerializedName("title")
    val title: String, // Website development
    @SerializedName("titleHtml")
    val titleHtml: String, // Website development
    @SerializedName("type")
    val type: String // collective
)