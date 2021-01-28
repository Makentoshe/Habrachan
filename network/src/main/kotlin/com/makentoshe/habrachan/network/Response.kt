package com.makentoshe.habrachan.network

import okhttp3.ResponseBody
import retrofit2.Response

inline fun <R> Response<ResponseBody>.fold(
    onSuccess: (body: ResponseBody) -> R,
    onFailure: (body: ResponseBody) -> R
): R {
//    contract {
//        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
//        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
//    }
    return if (isSuccessful) onSuccess(body()!!) else onFailure(errorBody()!!)
}
