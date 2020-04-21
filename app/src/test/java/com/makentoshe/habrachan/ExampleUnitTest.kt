package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.ImageRequest
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : BaseTest() {

    @Test
    @Ignore
    fun getUserAvatarTest() {
        val avatarUrl = "https://habrastorage.org/getpro/habr/avatars/6d5/142/fd3/6d5142fd38ef294711e6ecb9e764f8ed.png"
        val avatarRequest = ImageRequest(avatarUrl)
        val manager = ImageManager.Builder(OkHttpClient()).build()
        val response = manager.getImage(avatarRequest).blockingGet()
        println(response)
    }
}
