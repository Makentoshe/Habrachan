package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.entity.Vote
import com.makentoshe.habrachan.common.model.network.CookieStorage
import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class VotePostTest {

    private lateinit var votePost: VotePost
    private lateinit var api: HabrApi
    private lateinit var cookieStorage: CookieStorage
    private lateinit var factory: VotePostConverterFactory

    @Before
    fun init() {
        api = mockk()
        factory = VotePostConverterFactory()
        cookieStorage = mockk()
        votePost = VotePost(api, cookieStorage, factory)
    }

    @Test
    fun `should return success result`() {
        val body = mockk<Result.VotePostResponse>()

        val response = mockk<Response<Result.VotePostResponse>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns body

        val call = mockk<Call<Result.VotePostResponse>>()
        every { call.execute() } returns response

        every { api.votePostThroughApi(any(), any(), any(), any()) } returns call

        votePost.execute(VotePostRequest(123, Vote.VoteUp, "", "")).also {
            assertEquals(it, body)
        }
    }

    @Test
    fun `should return error result`() {
        val errorBody = mockk<ResponseBody>()
        every { errorBody.string() } returns "{\"data\":{}}"

        val response = mockk<Response<Result.VotePostResponse>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.errorBody() } returns errorBody

        val call = mockk<Call<Result.VotePostResponse>>()
        every { call.execute() } returns response

        every { api.votePostThroughApi(any(), any(), any(), any()) } returns call

        votePost.execute(VotePostRequest(123, Vote.VoteUp, "", "")).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}