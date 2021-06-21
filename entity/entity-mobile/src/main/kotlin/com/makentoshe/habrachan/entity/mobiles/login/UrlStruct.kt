package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class UrlStruct(
    @SerializedName("auth")
    val auth: Any?, // null
    @SerializedName("hash")
    val hash: Any?, // null
    @SerializedName("host")
    val host: Any?, // null
    @SerializedName("hostname")
    val hostname: Any?, // null
    @SerializedName("href")
    val href: String, // /en/all/
    @SerializedName("path")
    val path: String, // /en/all/
    @SerializedName("pathname")
    val pathname: String, // /en/all/
    @SerializedName("port")
    val port: Any?, // null
    @SerializedName("protocol")
    val protocol: Any?, // null
    @SerializedName("query")
    val query: Any,
    @SerializedName("slashes")
    val slashes: Any? // null
)