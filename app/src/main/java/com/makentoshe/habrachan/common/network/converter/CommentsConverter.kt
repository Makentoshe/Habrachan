package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import com.makentoshe.habrachan.common.entity.post.PostResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CommentsConverter {

    fun convertBody(body: ResponseBody): CommentsResponse {
        return Gson().fromJson(body.string(), CommentsResponse::class.java)
    }

    fun convertError(body: ResponseBody): String {
        return body.string()
    }
}
