package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.Statistics

data class Company(
    @SerializedName("alias")
    val alias: String, // ruvds
    @SerializedName("commonHubs")
    val commonHubs: List<CompanyHub>,
    @SerializedName("descriptionHtml")
    val descriptionHtml: String, // VDS/VPS-хостинг. Скидка 10% по коду <b>HABR10</b>
    @SerializedName("id")
    val id: String, // 3593
    @SerializedName("imageUrl")
    val imageUrl: String, // //habrastorage.org/getpro/habr/company/f27/ea8/5bd/f27ea85bd62fbd985c308110885af7e0.png
    @SerializedName("relatedData")
    val relatedData: CompanyRelatedData,
    @SerializedName("statistics")
    val statistics: Statistics,
    @SerializedName("titleHtml")
    val titleHtml: String // RUVDS.com
)