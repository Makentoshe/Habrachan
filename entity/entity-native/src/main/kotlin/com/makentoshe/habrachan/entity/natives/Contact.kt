package com.makentoshe.habrachan.entity.natives


import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("link")
    val link: String, // <a href="https://github.com/Makentoshe/" class="url">Makentoshe</a>
    @SerializedName("title")
    val title: String, // Github
    @SerializedName("value")
    val value: String // Makentoshe
)