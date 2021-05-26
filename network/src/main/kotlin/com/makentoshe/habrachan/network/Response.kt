package com.makentoshe.habrachan.network

import okhttp3.ResponseBody

inline fun <R> retrofit2.Response<ResponseBody>.fold(
    onSuccess: (body: ResponseBody) -> R, onFailure: (body: ResponseBody) -> R
): R {
//    contract {
//        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
//        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
//    }
    return if (isSuccessful) onSuccess(body()!!) else onFailure(errorBody()!!)
}

inline fun <R> okhttp3.Response.fold(
    onSuccess: (body: ResponseBody) -> R, onFailure: (body: ResponseBody) -> R
): R {
//    contract {
//        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
//        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
//    }
    return if (isSuccessful) onSuccess(body!!) else onFailure(body!!)
}
