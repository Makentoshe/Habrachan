package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Inputs(
    @SerializedName("agreement")
    val agreement: Agreement,
    @SerializedName("articlesLangEnglish")
    val articlesLangEnglish: ArticlesLang,
    @SerializedName("articlesLangRussian")
    val articlesLangRussian: ArticlesLang,
    @SerializedName("digest")
    val digest: Digest,
    @SerializedName("email")
    val email: Email,
    @SerializedName("uiLang")
    val uiLang: UiLang
)