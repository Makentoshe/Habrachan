package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.login.LoginResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostsConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, GetPostsResult> {
            Gson().fromJson(it.string(), GetPostsResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, GetPostsResult>? {
        return if (type == GetPostsResult::class.java) converter else null
    }

}

class StringConverterFactory: Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, String> { it.string() }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, String>? {
        return if (type == String::class.java) converter else null
    }
}

class ByteArrayConverterFactory: Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, ByteArray> { it.bytes() }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, ByteArray>? {
        return if (type.toString() == "byte[]" || type == ByteArray::class.java) converter else null
    }
}

class LoginConverterFactory: Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, LoginResult> {
            Gson().fromJson(it.string(), LoginResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, LoginResult>? {
        return if (type == LoginResult::class.java) converter else null
    }
}