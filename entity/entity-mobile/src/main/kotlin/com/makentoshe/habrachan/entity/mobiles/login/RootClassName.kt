package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class RootClassName(
    @SerializedName("classNamesDict")
    val classNamesDict: ClassNamesDict
)