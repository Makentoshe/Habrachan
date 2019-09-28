package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.HabrApi
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
        val result: GetPostsBySearchResult = mockk()

        val response: Response<GetPostsBySearchResult> = mockk()
        every { response.body() } returns result

        val call: Call<GetPostsBySearchResult> = mockk()
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

        val response: Response<GetPostsBySearchResult> = mockk()
        every { response.body() } returns null
        every { response.code() } returns code
        every { response.message() } returns message

        val call: Call<GetPostsBySearchResult> = mockk()
        every { call.execute() } returns response

        val habrApi: HabrApi = mockk()
        every { habrApi.getPostsBySearch(any(), any(), any(), any()) } returns call

        GetPostsBySearch(habrApi).execute(GetPostsBySearchRequest()).also {
                assertEquals(false, it.success)
                assertEquals(code, it.data.code)
                assertEquals(message, it.data.message)
                assertNull(it.data.articles)
                assertNull(it.data.pages)
            }
    }
}