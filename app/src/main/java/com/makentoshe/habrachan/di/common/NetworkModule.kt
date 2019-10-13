package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.common.database.RequestStorage
import com.makentoshe.habrachan.common.database.SharedRequestStorage
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NetworkScope

class NetworkModule(context: Context) : Module() {

    private val requestStorage = SharedRequestStorage.Builder().build(context)

    private val factory = GetPostsRequestFactory(
        client = "85cab69095196f3.89453480",
        api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        token = null,
        requestStorage = requestStorage
    )

    init {
        bind<RequestStorage>().toInstance(requestStorage)
        bind<GetPostsRequestFactory>().toInstance(factory)
    }
}