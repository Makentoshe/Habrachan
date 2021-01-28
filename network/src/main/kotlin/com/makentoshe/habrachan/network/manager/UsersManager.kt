package com.makentoshe.habrachan.network.manager

//interface UsersManager {
//
//    fun getMe(request: MeRequest): Result<UserResponse>
//
//    fun getUser(request: UserRequest): Result<UserResponse>
//
//    class Builder(private val client: OkHttpClient) {
//
//        private val baseUrl = "https://habr.com/"
//
//        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()
//
//        fun build(): UsersManager {
//            val api = getRetrofit().create(UsersApi::class.java)
//        }
//    }
//}
//
////class NativeUsersManager : UsersManager {
////
////    override fun getMe(request: MeRequest): Single<UserResponse> {
////        return Single.just(request).observeOn(Schedulers.io()).map { request ->
////            api.getMe(request.client, request.token, request.include, request.exclude).execute()
////        }.map { response ->
////            if (response.isSuccessful) {
////                UsersConverter().convertBody(response.body()!!)
////            } else {
////                UsersConverter().convertError(response.errorBody()!!)
////            }
////        }
////    }
////
////    override fun getUser(request: UserRequest): Single<UserResponse> {
////        return Single.just(request).observeOn(Schedulers.io()).map { request ->
////            api.getUser(
////                request.client, request.token, request.name, request.include, request.exclude
////            ).execute()
////        }.map { response ->
////            if (response.isSuccessful) {
////                UsersConverter().convertBody(response.body()!!)
////            } else {
////                UsersConverter().convertError(response.errorBody()!!)
////            }
////        }
////    }
////}
