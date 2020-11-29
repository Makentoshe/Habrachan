package com.makentoshe.habrachan.network.converter

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.entity.Badges
import com.makentoshe.habrachan.entity.Geo
import com.makentoshe.habrachan.network.deserializer.BadgesDeserializer
import com.makentoshe.habrachan.network.deserializer.GeoDeserializer
import com.makentoshe.habrachan.network.response.UserResponse
import okhttp3.ResponseBody

class UsersConverter : Converter<UserResponse> {

    override fun convertBody(body: ResponseBody): Result<UserResponse> {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer())
            .registerTypeAdapter(Geo::class.java, GeoDeserializer()).create()
        val response = gson.fromJson(body.string(), UserResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<UserResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}