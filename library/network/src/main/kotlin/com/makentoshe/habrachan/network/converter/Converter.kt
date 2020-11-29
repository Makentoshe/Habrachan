package com.makentoshe.habrachan.network.converter

import okhttp3.ResponseBody

interface Converter<T> {
    fun convertBody(body: ResponseBody): Result<T>

    fun convertError(body: ResponseBody): Result<T>
}