package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.posts.GetPostsBySearch
import com.makentoshe.habrachan.common.model.network.posts.GetPostsBySearchRequest
import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class GetPostsBySearchTest {

    @Test
    fun `should return a result`() {
        val result: GetPostsResult = mockk()

        val response: Response<GetPostsResult> = mockk()
        every { response.body() } returns result

        val call: Call<GetPostsResult> = mockk()
        every { call.execute() } returns response

        val habrApi: HabrApi = mockk()
        every { habrApi.getPostsBySearch(allAny(), any()) } returns call

        GetPostsBySearch(habrApi).execute(GetPostsBySearchRequest()).also {
                assertEquals(result, it)
            }
    }

    @Test
    fun `should return a result with error`() {
        val code = 21345
        val message = "asdfghkjl;htr"

        val response: Response<GetPostsResult> = mockk()
        every { response.body() } returns null
        every { response.code() } returns code
        every { response.message() } returns message

        val call: Call<GetPostsResult> = mockk()
        every { call.execute() } returns response

        val habrApi: HabrApi = mockk()
        every { habrApi.getPostsBySearch(any(), any(), any(), any()) } returns call

        GetPostsBySearch(habrApi).execute(GetPostsBySearchRequest()).also {
                assertEquals(false, it.success)
                assertEquals(code, it.data.code)
                assertEquals(message, it.data.message)
                assertNull(it.data.articles)
                assertNull(it.data.data)
                assertNull(it.data.pages)
            }
    }
}